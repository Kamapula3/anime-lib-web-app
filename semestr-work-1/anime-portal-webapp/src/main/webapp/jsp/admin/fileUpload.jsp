<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Загрузить обложку — Anime Lib</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mainPage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminPage.css">
    <style>
        body {
            padding: 20px;
            background-color: #f9f9fb;
        }

        .upload-container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            padding: 32px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        }

        h2 {
            margin-bottom: 24px;
            color: #333;
            font-size: 22px;
            text-align: center;
        }

        .form-group {
            margin-bottom: 20px;
            text-align: left;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #333;
        }

        input[type="file"] {
            width: 100%;
            padding: 10px;
            border: 2px dashed #ccc;
            border-radius: 8px;
            background: #fafafa;
            font-size: 16px;
            cursor: pointer;
        }

        input[type="file"]:focus {
            outline: none;
            border-color: #6a11cb;
        }

        button[type="submit"] {
            width: 100%;
            padding: 12px;
            background-color: #6a11cb;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s;
        }

        button[type="submit"]:hover {
            background-color: #5a0fb9;
        }

        .success-message {
            margin-top: 20px;
            padding: 12px;
            background-color: #e8f5e9;
            color: #2e7d32;
            border: 1px solid #a5d6a7;
            border-radius: 6px;
            text-align: center;
            font-weight: 500;
        }
    </style>
</head>
<body>
<div class="upload-container">
    <h2>Загрузка обложки для аниме #${param.id}</h2>

    <form method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${param.id}">

        <div class="form-group">
            <label>Выберите файл:</label>
            <input type="file" name="file" required accept="image/*">
        </div>

        <button type="submit">Загрузить</button>
    </form>

    <c:if test="${not empty added}">
        <p class="success-message"><c:out value="${added}" /></p>
    </c:if>
</div>
</body>
</html>