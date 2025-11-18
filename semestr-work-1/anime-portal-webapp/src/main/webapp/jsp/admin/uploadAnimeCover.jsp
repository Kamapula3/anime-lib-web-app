<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Загрузка обложек — Anime Lib</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mainPage.css">
    <style>
        .upload-container {
            max-width: 800px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .upload-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
            padding: 32px;
        }

        .anime-list {
            display: flex;
            flex-direction: column;
            gap: 20px;
            margin-top: 20px;
        }

        .anime-item {
            padding: 16px;
            border: 1px solid #eee;
            border-radius: 12px;
            background: #fafaff;
        }

        .anime-title {
            font-size: 18px;
            font-weight: 600;
            color: #6a11cb;
            margin-bottom: 6px;
        }

        .anime-item div {
            font-size: 15px;
            color: #555;
            margin-bottom: 8px;
        }

        .upload-link {
            display: inline-block;
            padding: 8px 16px;
            background: #f5f0ff;
            color: #6a11cb;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            border: 1px solid #caa0ff;
            transition: all 0.2s;
        }

        .upload-link:hover {
            background: #eae5ff;
            color: #5a0fb0;
        }

        .empty-message {
            text-align: center;
            font-size: 18px;
            color: #7a7a7a;
            font-style: italic;
            padding: 40px 20px;
        }

        .success-message {
            color: #155724;
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            padding: 0.75rem 1.25rem;
            border-radius: 0.375rem;
            margin-bottom: 1rem;
            font-weight: bold;
        }

    </style>
</head>
<body>
<header class="main-header">
    <div class="header-content">
        <img src="${pageContext.request.contextPath}/img/logo.png" class="logo">
        <h1 class="site-title">Загрузка обложек</h1>
        <a href="${pageContext.request.contextPath}/adminPage" class="btn btn-back">← Назад</a>
    </div>
</header>

<main class="upload-container">
    <div class="upload-card">
        <c:choose>
            <c:when test="${empty animeWithoutCover}">
                <div class="empty-message">
                    Все аниме уже имеют обложки!
                </div>
            </c:when>
            <c:otherwise>
                <p>Найдено <strong>${animeWithoutCover.size()}</strong> аниме без обложки:</p>
                <div class="anime-list">
                    <c:forEach items="${animeWithoutCover}" var="anime">
                        <div class="anime-item">
                            <div class="anime-title">${anime.title}</div>
                            <div>Год: ${anime.releaseYear}</div>
                            <a href="${pageContext.request.contextPath}/files?id=${anime.id}" class="upload-link">
                                Загрузить обложку
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<c:if test="${not empty added}">
    <p class="success-message"><c:out value="${added}" /></p>
</c:if>

</body>
</html>