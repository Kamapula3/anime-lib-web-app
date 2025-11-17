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
            <label>Название жанра *</label>
            <input type="text" name="genreName" placeholder="Например: Экшен" required>
        </div>

        <button type="submit" class="btn btn-submit">Добавить жанр</button>
    </form>

    <c:if test="${not empty error}">
        <p class="error-message"><c:out value="${error}" /></p>
    </c:if>

    <div class="genres-list">
        <h2>Список уже существующих жанров: (<c:out value="${fn:length(genresList)}" />)</h2>

        <c:choose>
            <c:when test="${not empty genresList}">
                <table class="genres-table">
                    <thead>
                    <tr>
                        <th>Жанр</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="genre" items="${genresList}">
                        <tr>
                            <td>
                                <span class="genre-tag"><c:out value="${genre}" /></span>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="no-genres">Жанров пока нет</p>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>
