<%--
Created by IntelliJ IDEA.
User: osint
Date: 2020-01-31
Time: 2:16 p.m.
To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html dir="ltr" lang="en-US">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="author" content="SemiColonWeb"/>

    <!-- Stylesheets
    ============================================= -->
    <jsp:include page="../styles.jsp"/>

    <!-- Document Title
    ============================================= -->
    <title>Rentier</title>

</head>

<body class="stretched">

<!-- Document Wrapper
============================================= -->
<div id="wrapper" class="clearfix">

    <!-- Header
============================================= -->
    <jsp:include page="header.jsp"/>

    <!-- Page Title
============================================= -->
    <section id="page-title">

        <div class="container clearfix">
            <h1>Sklepy sprzedawców</h1>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="row clearfix">

                    <div class="col-lg-12">

                        <h4>Sklepy dostęþne dla użytkownika ${user.firstName} ${user.lastName}</h4>

                        <form action="${pageContext.request.contextPath}/admin/users/shops" method="post">
                            <input type="hidden" value="${user.id}" name="userId"/>
                            <select name="userShops" multiple="multiple">
                                <c:forEach items="${shops}" var="shop">
                                    <option value="${shop.id}"
                                            <c:if test="${user.shops.contains(shop)}">selected</c:if>>${shop.shopName}</option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="button button-mini button-blue button-3d"
                                    value="Submit">Zapisz
                            </button>
                            <a href="javascript:window.history.back()" class="button button-mini button-blue button-3d">Wstecz</a>
                        </form>
                    </div>
                    <div class="w-100 line d-block d-md-none"></div>

                </div>

            </div>

        </div>

    </section><!-- #content end -->

    <!-- Footer
============================================= -->
    <jsp:include page="footer.jsp"/>

</div><!-- #wrapper end -->

    <jsp:include page="../scripts.jsp"/>

</body>
</html>
