<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Добавить персонажа — Anime Lib</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mainPage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<header class="main-header">
    <div class="header-content">
        <img src="${pageContext.request.contextPath}/img/logo.png" class="logo">
        <h1 class="site-title">Добавить персонажа</h1>
        <a href="${pageContext.request.contextPath}/adminPage" class="btn btn-back">← Назад</a>
    </div>
</header>

<main class="form-container">
    <form method="POST" action="${pageContext.request.contextPath}/addCharacter">
        <div class="form-group">
            <label>Имя *</label>
            <input type="text" name="name" placeholder="Например: Эрен" required>
        </div>

        <div class="form-group">
            <label>Фамилия *</label>
            <input type="text" name="surname" placeholder="Например: Йегер" required>
        </div>

        <div class="form-group">
            <label>Описание *</label>
            <textarea name="description" placeholder="Краткое описание персонажа..." required></textarea>
        </div>

        <div class="form-group">
            <label>Аниме *</label>
            <select name="animeName" required>
                <option value="">Обязательно выберите аниме</option>
                <c:forEach var="anime" items="${animeList}">
                    <option value="${anime.title}">${anime.title}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="btn btn-submit">Добавить персонажа</button>
    </form>

    <c:if test="${not empty error}">
        <p class="error-message"><c:out value="${error}" /></p>
    </c:if>

    <div class="characters-list">
        <h2>Список уже существующих персонажей: (<c:out value="${fn:length(characters)}" />)</h2>

        <c:choose>
            <c:when test="${not empty characters}">
                <table class="characters-table">
                    <thead>
                    <tr>
                        <th>Имя</th>
                        <th>Фамилия</th>
                        <th>Описание</th>
                        <th>Аниме</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="character" items="${characters}">
                        <tr>
                            <td><c:out value="${character.name}" /></td>
                            <td><c:out value="${character.surname}" /></td>
                            <td class="description-cell"><c:out value="${character.description}" /></td>
                            <td>
                                <c:if test="${character.anime_id != null}">
                                    <c:out value="${character.anime_id}" />
                                </c:if>
                                <c:if test="${character.anime_id == null}">—</c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="no-characters">Персонажей пока нет</p>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>