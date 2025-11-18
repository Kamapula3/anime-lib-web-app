<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body>
<div class="auth-container">
    <h2>Регистрация</h2>
    <form action="/signUp" method="post">
        <div class="input-group">
            <input type="text" name="username" placeholder="Имя пользователя" required
                   value="<c:out value="${param.username}"/>">
        </div>
        <div class="input-group">
            <input type="email" name="email" placeholder="Email" required
                   value="<c:out value="${param.email}"/>">
        </div>
        <div class="input-group">
            <input type="password" name="password" placeholder="Пароль" required>
        </div>
        <button type="submit" class="btn-submit">Зарегистрироваться</button>
    </form>

    <c:if test="${not empty error}">
        <p class="error-message"><c:out value="${error}" /></p>
    </c:if>

    <div class="auth-footer">
        <p>Уже есть аккаунт? <a href="/signIn">Войти</a></p>
    </div>
</div>
</body>
</html>