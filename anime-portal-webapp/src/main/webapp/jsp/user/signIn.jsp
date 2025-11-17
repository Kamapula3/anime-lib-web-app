<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body>
<div class="auth-container">
    <h2>Вход в аккаунт</h2>
    <form action="/signIn" method="post">
        <div class="input-group">
            <input
                    type="text"
                    name="login"
                    placeholder="Email"
                    required
                    value="<c:out value="${param.login}"/>"
            >
        </div>
        <div class="input-group">
            <input
                    type="password"
                    name="password"
                    placeholder="Пароль"
                    required
            >
        </div>
        <button type="submit" class="btn-submit">Войти</button>
    </form>

    <c:if test="${not empty error}">
        <p class="error-message"><c:out value="${error}" /></p>
    </c:if>

    <div class="auth-footer">
        <p>Нет аккаунта? <a href="/signUp">Зарегистрироваться</a></p>
    </div>
</div>
</body>
</html>
