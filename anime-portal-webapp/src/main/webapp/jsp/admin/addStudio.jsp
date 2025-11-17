<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Добавить аниме — Anime Lib</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mainPage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<header class="main-header">
    <div class="header-content">
        <img src="${pageContext.request.contextPath}/img/logo.png" class="logo">
        <h1 class="site-title">Добавить аниме</h1>
        <a href="${pageContext.request.contextPath}/adminPage" class="btn btn-back">← Назад</a>
    </div>
</header>

<main class="form-container">
    <form method="POST">

        <div class="form-group">
            <label>Название студии *</label>
            <input type="text" name="name" placeholder="Например: MAPPA" required>
        </div>

        <div class="form-group">
            <label>Страна *</label>
            <input type="text" name="country" placeholder="Например: Япония" required>
        </div>

        <button type="submit" class="btn btn-submit">Добавить студию</button>
    </form>

    <c:if test="${not empty error}">
        <p class="error-message"><c:out value="${error}" /></p>
    </c:if>

    <div class="studios-list">
        <h2>Список уже существующих студий: (<c:out value="${fn:length(studios)}" />)</h2>

        <c:choose>
            <c:when test="${not empty studios}">
                <table class="studios-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Название</th>
                        <th>Страна</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="studio" items="${studios}">
                        <tr>
                            <td><c:out value="${studio.id}" /></td>
                            <td><c:out value="${studio.name}" /></td>
                            <td><c:out value="${studio.country}" /></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="no-studios">Студий пока нет</p>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>