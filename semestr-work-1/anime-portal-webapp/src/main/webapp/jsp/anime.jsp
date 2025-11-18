<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Аниме — ${anime.title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/anime.css">
</head>
<body>
<div class="top-bar">
    <c:if test="${sessionScope.inProfile}">
        <a href="${pageContext.request.contextPath}/profile" class="back-link">← К профилю</a>
    </c:if>
    <c:if test="${!sessionScope.inProfile}">
        <a href="${pageContext.request.contextPath}/mainPage" class="back-link">← На главную</a>
    </c:if>
</div>

<c:choose>
    <c:when test="${not empty anime}">
        <div class="anime-card">
            <div class="anime-header">
                <h1 class="anime-title">${anime.title}</h1>
                <span class="release-year">(${anime.releaseYear})</span>
            </div>
            <c:choose>
                <c:when test="${hasCover}">
                    <img src="${pageContext.request.contextPath}/uploaded/files?id=${coverFileId}" class="anime-cover">
                </c:when>
                <c:otherwise>
                    <div class="no-cover">Нет обложки</div>
                </c:otherwise>
            </c:choose>
            <p class="anime-description">${anime.description}</p>

            <div class="anime-info">
                <p class="anime-rating">
                    <strong>Рейтинг:</strong> <span class="rating-value">${anime.rating}</span>
                </p>

                <c:if test="${sessionScope.inProfile}">
                    <div class="rating-section">
                        <h3>Ваша оценка: ${requestScope.score}</h3>
                        <form action="${pageContext.request.contextPath}/animeInfo" method="post">
                            <input type="hidden" name="animeId" value="${anime.id}">
                            <select name="rating" required>
                                <c:forEach begin="1" end="10" var="i">
                                    <option value="${i}">${i}</option>
                                </c:forEach>
                            </select>
                            <button type="submit">Оценить</button>
                        </form>
                    </div>
                </c:if>

                <c:if test="${!sessionScope.inProfile && !requestScope.inFavorite}">
                    <div>
                        <form action="${pageContext.request.contextPath}/animeInfo" method="post">
                            <input type="hidden" name="animeId" value="${anime.id}">
                            <input type="hidden" name="action" value="favorite">
                            <button type="submit">Добавить в избранное</button>
                        </form>
                    </div>
                </c:if>

                <c:if test="${requestScope.inFavorite}">
                    <p>Добавлено в избранное</p>
                    <div>
                        <form action="${pageContext.request.contextPath}/animeInfo" method="post">
                            <input type="hidden" name="animeId" value="${anime.id}">
                            <input type="hidden" name="action" value="delete_favorite">
                            <button type="submit">Удалить из избранного</button>
                        </form>
                    </div>
                </c:if>

                <p class="anime-genres">
                    <strong>Жанры:</strong>
                    <c:forEach var="genre" items="${anime.genres}" varStatus="status">
                        <span class="genre-tag">${genre}</span>
                    </c:forEach>
                </p>

                <p class="anime-studio">
                    <strong>Студия:</strong> <span class="studio-name">${anime.studio.name}</span>,
                    <span class="studio-country">${anime.studio.country}</span>
                </p>
            </div>

            <c:if test="${sessionScope.profile != null}">
                <div class="comment-form-section">
                    <h3>Оставить комментарий</h3>
                    <form action="${pageContext.request.contextPath}/animeInfo" method="post">
                        <input type="hidden" name="animeId" value="${anime.id}">
                        <input type="hidden" name="action" value="write_comment">
                        <textarea name="comment_text" rows="3" cols="60" required
                                  placeholder="Ваш комментарий..."></textarea>
                        <button type="submit">Отправить</button>
                    </form>

                    <c:if test="${not empty requestScope.error}">
                        <p class="error-message-inline">${requestScope.error}</p>
                    </c:if>
                </div>
            </c:if>

            <div class="comments-section">
                <h2>Комментарии (<c:out value="${fn:length(commentList)}" />)</h2>
                <c:choose>
                    <c:when test="${not empty commentList}">
                        <div class="comments-list">
                            <c:forEach var="comment" items="${commentList}">
                                <div class="comment-item">
                                    <div class="comment-header">
                                        <strong>Пользователь ${comment.username}</strong>
                                        <span class="comment-date">${comment.createdAt}</span>
                                    </div>
                                    <div class="comment-text">
                                        <c:out value="${comment.text}" />
                                    </div>

                                    <c:if test="${comment.userId == sessionScope.userId}">
                                        <form action="${pageContext.request.contextPath}/animeInfo" method="post">
                                            <input type="hidden" name="animeId" value="${anime.id}">
                                            <input type="hidden" name="action" value="delete_comment">
                                            <button type="submit">Удалить</button>
                                        </form>
                                    </c:if>

                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="no-comments">Комментариев пока нет. Будьте первым!</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="anime-characters-section">
                <h2>Персонажи</h2>
                <c:choose>
                    <c:when test="${not empty charactersList}">
                        <div class="characters-list">
                            <c:forEach var="character" items="${charactersList}">
                                <div class="character-item">
                                    <strong>${character.name} ${character.surname}</strong><br>
                                    <em>${character.description}</em>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="no-characters">Персонажи не найдены.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="error-message">
            <h2>Аниме не найдено.</h2>
            <a href="${pageContext.request.contextPath}/mainPage" class="back-link block">Вернуться на главную</a>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>