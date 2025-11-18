<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Управление пользователями — Anime Lib</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mainPage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<header class="main-header">
    <div class="header-content">
        <img src="${pageContext.request.contextPath}/img/logo.png" class="logo">
        <h1 class="site-title">Пользователи</h1>
        <a href="${pageContext.request.contextPath}/adminPage" class="btn btn-back">← Назад</a>
    </div>
</header>

<main class="form-container">
    <form method="POST" action="${pageContext.request.contextPath}/addUser">
        <div class="form-group">
            <label>Имя пользователя *</label>
            <input type="text" name="username" required>
        </div>

        <div class="form-group">
            <label>Email *</label>
            <input type="email" name="email" required>
        </div>

        <div class="form-group">
            <label>Пароль *</label>
            <input type="password" name="password" required>
        </div>

        <div class="form-group role-select">
            <label>Роль:</label>
            <label>
                <input type="radio" name="role" value="1" checked> Пользователь
            </label>
            <label>
                <input type="radio" name="role" value="2"> Администратор
            </label>
        </div>

        <button type="submit" class="btn btn-submit">Добавить пользователя</button>
    </form>

    <c:if test="${not empty error}">
        <p class="error-message"><c:out value="${error}" /></p>
    </c:if>

    <div class="users-list">
        <h2>Список пользователей: (<c:out value="${fn:length(users)}" />)</h2>

        <c:choose>
            <c:when test="${not empty users}">
                <table class="users-table">
                    <thead>
                    <tr>
                        <th>Имя</th>
                        <th>Email</th>
                        <th>Роль</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td><c:out value="${user.username}" /></td>
                            <td><c:out value="${user.email}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${user.role == 'ADMIN'}">
                                        <span class="role-tag role-admin">Админ</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="role-tag role-user">Пользователь</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="no-users">Пользователей пока нет</p>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>