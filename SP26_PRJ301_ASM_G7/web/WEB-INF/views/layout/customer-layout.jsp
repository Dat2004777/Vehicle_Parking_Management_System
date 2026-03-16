<%-- 
    Document   : customerToast
    Created on : Mar 5, 2026, 7:19:00 PM
    Author     : ADMIN
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
<title>JSP Page</title>
<style>
    :root {
        --bs-body-bg: #f3f4f6;
        --bs-primary: #1a56db;
        --bs-primary-rgb: 26, 86, 219;
        --bs-font-sans-serif: 'Inter', sans-serif;
        --text-main: #374151;
        --text-muted: #6b7280;
    }

    body {
        font-family: var(--bs-font-sans-serif);
        color: var(--text-main);
        -webkit-font-smoothing: antialiased;
        background-color: var(--bs-body-bg);
    }

</style>
<!-- Toast container -->

<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%-- Gọi taglib để nó in ra HTML và hàm showToast() --%>
<my:toast type="${type}" message="${message}" />

