package com.coursemanage.module.log.aspect;

import com.coursemanage.module.log.annotation.LogOperation;
import com.coursemanage.module.log.pojo.LogEntity;
import com.coursemanage.module.log.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogAspect {
    private final LogService logService;
    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private String getCurrentSchoolNum(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }
    @Around("@annotation(logOperation)")
    public Object around(ProceedingJoinPoint joinPoint, LogOperation logOperation) throws Throwable {
        Object result = null;
        boolean success = false;
        Exception exception = null;

        try {
            result = joinPoint.proceed();
            success = true;
            return result;

        } catch (Exception e) {
            exception = e;
            throw e;

        } finally {
            try {
                recordOperationLog(joinPoint, logOperation, success, result, exception);
            } catch (Exception e) {
                log.error("记录操作日志失败，但不影响主业务流程", e);
            }
        }
    }

    private void recordOperationLog(ProceedingJoinPoint joinPoint, LogOperation logOperation,
                                    boolean success, Object result, Exception exception) {
        try {
            StandardEvaluationContext context = buildEvaluationContext(joinPoint, result, exception);

            String operatorSchoolNum = evaluateExpressionSafely(context, logOperation.operator(), getCurrentSchoolNum());
            String targetSchoolNum = evaluateExpressionSafely(context, logOperation.target(), null);
            String module = evaluateExpressionSafely(context, logOperation.module(), logOperation.module());
            String action = evaluateExpressionSafely(context, logOperation.action(), logOperation.action());
            String detailExpression = success ? logOperation.detailOnSuccess() : logOperation.detailOnFailure();
            String detailInfo = evaluateExpressionSafely(context, detailExpression, success?logOperation.detailOnSuccess():logOperation.detailOnFailure());

            LogEntity logEntity = LogEntity.builder()
                    .operationTime(new Date())
                    .operatorSchoolNum(operatorSchoolNum)
                    .targetSchoolNum(targetSchoolNum)
                    .detailInfo(detailInfo)
                    .operationModule(module)
                    .operationAction(action)
                    .build();

            saveLogSafely(logEntity);

        } catch (Exception e) {
            log.error("构建操作日志记录失败", e);
        }
    }

    private StandardEvaluationContext buildEvaluationContext(ProceedingJoinPoint joinPoint,
                                                             Object result, Exception exception) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 设置方法参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        // 设置其他有用变量
        context.setVariable("result", result);
        context.setVariable("exception", exception);
        context.setVariable("methodName", signature.getMethod().getName());
        context.setVariable("target", joinPoint.getTarget());
        return context;
    }

    private String evaluateExpressionSafely(StandardEvaluationContext context,
                                            String expression, String defaultValue) {
        try {
            Expression exp = expressionParser.parseExpression(expression);
            Object value = exp.getValue(context);
            return value.toString();

        } catch (Exception e) {
            log.info("SpEL表达式解析失败: {}, 将使用默认值: {}", expression, defaultValue);
            return defaultValue;
        }
    }

    private void saveLogSafely(LogEntity logEntity) {
        try {
            logService.addOne(logEntity);
        } catch (Exception e) {
            log.error("保存操作日志失败", e);
        }
    }
}
