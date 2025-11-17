<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/startPage.css">

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <title>Anime Lib — поиск аниме</title>

    <script>
        const ctx = "${pageContext.request.contextPath}";
    </script>

    <script>
        function loadAnime() {
            let filters = {
                title: $("#title").val(),
                genre: $("#genre").val(),
                studio: $("#studio").val(),
                year: $("#year").val()
            };

            $.ajax({
                url: ctx + "/ajax/anime",
                type: "GET",
                data: filters,
                success: function (animeList) {
                    renderAnime(animeList);
                },
                error: function () {
                    $(".anime-grid").html("<p class='no-anime'>Ошибка загрузки данных</p>");
                }
            });
        }

        function renderAnime(list) {
            var container = $(".anime-grid");
            var html = "";

            if (!list || list.length === 0) {
                container.html("<p class='no-anime'>Аниме пока нет в базе.</p>");
                return;
            }

            list.forEach(function(a) {
                html += '<a href="' + ctx + '/signUp" class="anime-card-link">';
                html += '  <div class="anime-card">';

                html += '    <div class="anime-header">';
                html += '      <h3 class="anime-title">' + escapeHtml(a.title) + '</h3>';
                html += '      <span class="release-year">(' + (a.releaseYear || "") + ')</span>';
                html += '    </div>';

                if (a.coverFileId != null) {
                    html += '<img src="' + ctx + '/uploaded/files?id=' + a.coverFileId + '" class="anime-cover">';
                } else {
                    html += '<div class="anime-cover-placeholder">Нет обложки</div>';
                }

                html += '    <p class="anime-rating"><strong>Рейтинг:</strong> <span>' + (a.rating || "") + '</span></p>';

                var genres = Array.isArray(a.genres) ? a.genres.join(", ") : "";
                html += '    <p class="anime-genres"><strong>Жанры:</strong> ' + escapeHtml(genres) + '</p>';

                var studioName = (a.studio && a.studio.name) ? a.studio.name : "";
                html += '    <p class="anime-studio"><strong>Студия:</strong> ' + escapeHtml(studioName) + '</p>';

                html += '  </div>';
                html += '</a>';
            });

            container.html(html);
        }

        function escapeHtml(s) {
            if (s == null) return "";
            return String(s)
                .replace(/&/g, "&amp;")
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;")
                .replace(/"/g, "&quot;")
                .replace(/'/g, "&#039;");
        }

        $(document).ready(function() {
            loadAnime();

            $(".btn-filter").on("click", function (e) {
                e.preventDefault();
                loadAnime();
            });

            $(".btn-clear").on("click", function (e) {
                e.preventDefault();
                $("#title").val("");
                $("#genre").val("");
                $("#studio").val("");
                $("#year").val("");
                loadAnime();
            });
        });
    </script>
</head>

<body>
<header class="main-header">
    <div class="header-content">
        <img src="${pageContext.request.contextPath}/img/logo.png" class="logo">
        <h1 class="site-title">Anime Lib</h1>

        <div class="auth-buttons">
            <a href="${pageContext.request.contextPath}/signIn" class="btn btn-login">Войти</a>
            <a href="${pageContext.request.contextPath}/signUp" class="btn btn-register">Регистрация</a>
        </div>
    </div>
</header>

<main class="anime-grid-container">

    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>

    <div class="intro-text">
        <p>
            Уже много лет японская анимация пользуется огромной популярностью по всему миру — и в России в том числе. Зрителей привлекают яркие сюжеты, уникальная стилистика и эмоциональная глубина, присущая аниме.
            Однако со временем список просмотренных тайтлов может вырасти настолько, что легко забыть — смотрели вы конкретное аниме или нет. А когда вы обсуждаете любимые сериалы с друзьями, бывает неудобно не иметь под рукой своего вишлиста или списка «просмотрено». Хранить всё в заметках — не всегда удобно и надёжно.
            Именно для этого и создан наш сайт. Здесь вы можете собрать всю свою коллекцию аниме в одном месте: добавлять тайтлы в профиль, указывать их статус («запланировано», «просмотрено», «в процессе» и т.д.), ставить оценки и оставлять комментарии — чтобы не забыть детали и помочь другим зрителям сделать выбор.
        </p>
    </div>

    <h2 class="page-title">Список аниме</h2>

    <form class="filter-form">

        <div class="filter-group">
            <label>Название:</label>
            <input type="text" id="title" class="filter-input" placeholder="Введите название">
        </div>

        <div class="filter-group">
            <label>Жанр:</label>
            <select id="genre" class="filter-select">
                <option value="">Любой</option>
                <c:forEach var="g" items="${genres}">
                    <option value="${g}">${g}</option>
                </c:forEach>
            </select>
        </div>

        <div class="filter-group">
            <label>Студия:</label>
            <select id="studio" class="filter-select">
                <option value="">Любая</option>
                <c:forEach var="s" items="${studios}">
                    <option value="${s.name}">${s.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="filter-group">
            <label>Год:</label>
            <select id="year" class="filter-select">
                <option value="">Любой</option>
                <c:forEach begin="${minYear}" end="${maxYear}" var="y">
                    <option value="${y}">${y}</option>
                </c:forEach>
            </select>
        </div>

        <button class="btn-filter">Применить</button>
        <button class="btn-clear">Сбросить</button>
    </form>

    <div class="anime-grid"></div>

</main>
</body>
</html>
