<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Профиль</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <script>
        const ctx = "${pageContext.request.contextPath}";
    </script>

    <script>

        function loadFavorites() {
            $.ajax({
                url: ctx + "/ajax/profile/favorites",
                type: "GET",
                success: function (list) {
                    renderFavorites(list);
                },
                error: function () {
                    $(".anime-list").html("<p class='no-anime'>Ошибка загрузки избранного</p>");
                }
            });
        }

        function renderFavorites(list) {
            let container = $(".anime-list");
            let counter = $(".favorites-count");

            const count = list ? list.length : 0;
            counter.text(count);

            let html = "";

            if (!list || list.length === 0) {
                container.html("<p class='no-anime'>У вас пока нет избранных аниме.</p>");
                return;
            }

            list.forEach(function(a) {

                html += '<div class="anime-card-block">';

                html += '<a href="' + ctx + '/animeInfo?id=' + a.id + '" class="anime-card-link">';
                html += '  <div class="anime-card">';

                html += '    <div class="anime-title">';
                html += '      <h3>' + escapeHtml(a.title) + '</h3>';
                html += '      <span class="release-year">(' + (a.releaseYear || "") + ')</span>';
                html += '    </div>';

                html += '    <div class="anime-details">';
                html += '      <p><strong>Студия:</strong> ' + escapeHtml(a.studio ? a.studio.name : "") + '</p>';
                html += '      <p><strong>Рейтинг:</strong> <span>' + (a.rating || "") + '</span></p>';
                html += '      <p><strong>Жанры:</strong> ' + escapeHtml((a.genres || []).join(", ")) + '</p>';
                html += '    </div>';

                html += '    <div class="anime-status">';
                html += '      <p><strong>Ваш статус:</strong> <span>' + escapeHtml(a.status) + '</span></p>';
                html += '    </div>';

                html += '  </div>';
                html += '</a>';

                html += '<div class="status-control">';
                html += '  <select class="status-select" data-id="' + a.id + '">';
                html +=        renderStatusOptions(a.status);
                html += '  </select>';
                html += '  <button class="btn-update-status" data-id="' + a.id + '">Обновить</button>';
                html += '</div>';

                html += '</div>';

            });

            container.html(html);

            $(".btn-update-status").on("click", function () {
                let id = $(this).data("id");
                let newStatus = $("select[data-id='" + id + "']").val();
                updateStatus(id, newStatus);
            });
        }


        function renderStatusOptions(current) {
            const statuses = ["В планах", "Смотрю", "Просмотрено", "Брошено"];
            let html = "";

            statuses.forEach(function(s) {
                html += '<option value="' + s + '" ' + (s === current ? 'selected' : '') + '>' + s + '</option>';
            });

            return html;
        }


        function updateStatus(animeId, status) {
            $.ajax({
                url: ctx + "/ajax/profile/status",
                type: "POST",
                data: { animeId, status },
                success: function () {
                    loadFavorites();
                },
                error: function () {
                    alert("Ошибка обновления статуса");
                }
            });
        }

        function escapeHtml(s) {
            if (!s) return "";
            return String(s)
                .replace(/&/g, "&amp;")
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;")
                .replace(/"/g, "&quot;")
                .replace(/'/g, "&#039;");
        }

        $(document).ready(function () {
            loadFavorites();
        });

    </script>

</head>

<body>
<div class="container">

    <header class="profile-header">
        <h1>Профиль пользователя</h1>
        <a href="${pageContext.request.contextPath}/mainPage" class="back-link">← Назад</a>
    </header>

    <div class="user-info">
        <h2 class="username">${profile.username}</h2>
        <p><strong>Email:</strong> ${profile.email}</p>
        <p><strong>Дата регистрации:</strong> ${profile.regDate}</p>
    </div>

    <div class="anime-list-section">
        <h2>Избранные аниме (<span class="favorites-count">0</span>)</h2>
        <div class="anime-list"></div>
    </div>

</div>
</body>
</html>
