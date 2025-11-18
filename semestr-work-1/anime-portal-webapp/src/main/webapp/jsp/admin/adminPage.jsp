<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Админ-панель — Anime Lib</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mainPage.css">
</head>
<body>
<header class="main-header">
    <div class="header-content">
        <img src="${pageContext.request.contextPath}/img/logo.png" class="logo">
        <h1 class="site-title">Админ-панель</h1>
        <div class="admin-nav">
            <a href="${pageContext.request.contextPath}/mainPage" class="btn btn-back">← На главную</a>
        </div>
    </div>
</header>

<main class="admin-container">
    <div class="admin-grid">
        <a href="${pageContext.request.contextPath}/addAnime" class="admin-card">
            <h3>Добавить аниме</h3>
        </a>
        <a href="${pageContext.request.contextPath}/addStudio" class="admin-card">
            <h3>Добавить студию</h3>
        </a>
        <a href="${pageContext.request.contextPath}/addGenre" class="admin-card">
            <h3>Добавить жанр</h3>
        </a>
        <a href="${pageContext.request.contextPath}/addCharacter" class="admin-card">
            <h3>Добавить персонажа</h3>
        </a>
        <a href="${pageContext.request.contextPath}/addUser" class="admin-card">
            <h3>Добавить пользователя</h3>
        </a>
        <a href="${pageContext.request.contextPath}/uploadAnimeCover" class="admin-card">
            <h3>Добавить обложки к аниме</h3>
        </a>
    </div>

    <c:if test="${not empty error}">
        <p class="error-message"><c:out value="${error}" /></p>
    </c:if>

    <c:if test="${not empty added}">
        <p class="success-message"><c:out value="${added}" /></p>
    </c:if>

</main>
</body>
</html>