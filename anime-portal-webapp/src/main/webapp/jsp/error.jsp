
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Ошибка</title></head>
<body>
<h2>Произошла ошибка</h2>
<p>${error}</p>
<a href="${pageContext.request.contextPath}/">На главную</a>
</body>
</html>