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
    <form method="POST" action="${pageContext.request.contextPath}/addAnime">

        <div class="form-group">
            <label>Название аниме *</label>
            <input type="text" name="title" placeholder="Например: Атака титанов" required>
        </div>

        <div class="form-group">
            <label>Описание *</label>
            <textarea name="description" placeholder="Краткое описание сюжета..." required></textarea>
        </div>

        <div class="form-group">
            <label>Год выпуска *</label>
            <input type="number" name="year" min="1900" max="2030" placeholder="2023" required>
        </div>

        <div class="form-group">
            <label>Студия *</label>
            <select name="studio" required>
                <option value="">Обязательно выберите студию</option>
                <c:forEach var="studio" items="${studios}">
                    <option value="${studio.name}">${studio.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Жанры * (выберите хотя бы один)</label>
            <div class="checkbox-group">
                <c:forEach var="genre" items="${genres}" varStatus="status">
                    <label class="checkbox-item">
                        <input type="checkbox" name="genres" value="${genre}">
                        <span class="checkbox-custom"></span>
                        <span class="checkbox-label">${genre}</span>
                    </label>
                </c:forEach>
            </div>
        </div>

        <button type="submit" class="btn btn-submit">Добавить аниме</button>
    </form>

    <c:if test="${not empty error}">
        <p class="error-message"><c:out value="${error}" /></p>
    </c:if>

    <div class="anime-list">
        <h2>Список уже существующих аниме: (<c:out value="${fn:length(animeList)}" />)</h2>

        <c:choose>
            <c:when test="${not empty animeList}">
                <table class="anime-table">
                    <thead>
                    <tr>
                        <th>Название</th>
                        <th>Год</th>
                        <th>Рейтинг</th>
                        <th>Студия</th>
                        <th>Жанры</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="anime" items="${animeList}">
                        <tr>
                            <td><c:out value="${anime.title}" /></td>
                            <td><c:out value="${anime.releaseYear}" /></td>
                            <td class="rating-cell">
                                <c:if test="${anime.rating != null}">
                                    <c:out value="${anime.rating}" />
                                </c:if>
                                <c:if test="${anime.rating == null}">—</c:if>
                            </td>
                            <td>
                                <c:if test="${anime.studio != null}">
                                    <c:out value="${anime.studio.name}" />
                                </c:if>
                                <c:if test="${anime.studio == null}">—</c:if>
                            </td>
                            <td class="genres-cell">
                                <c:if test="${not empty anime.genres}">
                                    <c:forEach var="genre" items="${anime.genres}">
                                        <span class="genre-tag"><c:out value="${genre}" /></span>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty anime.genres}">
                                    <span>—</span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="no-anime">Аниме пока нет</p>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>