package com.example.homework5.aspects

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LogExecutionTimeAspect {
    private val logger = LoggerFactory.getLogger(LogExecutionTimeAspect::class.java)

    @Pointcut("@annotation(com.example.homework5.aspects.LogExecutionTime)")
    fun hasAnnotation() {  }

    @Around("hasAnnotation()")
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val start = System.currentTimeMillis()

        val result = joinPoint.proceed()

        val elapsedTime = System.currentTimeMillis() - start

        val methodSignature = joinPoint.signature as MethodSignature
        val methodName = methodSignature.method.name
        val className = methodSignature.declaringTypeName

        logger.info("Executed method [$className.$methodName] in $elapsedTime ms")

        return result
    }
}