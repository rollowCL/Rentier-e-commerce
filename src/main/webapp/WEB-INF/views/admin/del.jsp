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
            <h1>Konfiguracja</h1>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="#">Home</a></li>
                <li class="breadcrumb-item active" aria-current="page">Konfiguracja</li>
            </ol>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="row clearfix">


                    <div class="col-lg-12">

                        <div class="heading-block nobottomborder">
                        </div>
                        <div>
                            <h3>Czy na pewno chesz usunąć
                            <c:if test="${not empty shop.id}"> sklep <strong><c:out value="${shop.shopName}"/></strong> z całym stanem magazynowym?</c:if>
                            <c:if test="${not empty brand.id}"> markę <strong><c:out value="${brand.name}"/></strong> z całym asortymentem i stanem magazynowym?</c:if>
                            <c:if test="${not empty productCategory.id}"> kategorię <strong><c:out value="${productCategory.categoryName}"/></strong> z całym asortymentem i stanem magazynowym?</c:if>
                            <c:if test="${not empty productSize.id}"> rozmiar <strong><c:out value="${productSize.sizeName}"/></strong> z całym asortymentem i i stanem magazynowym?</c:if>
                            <c:if test="${not empty paymentMethod.id}"> metodę płatności <strong><c:out value="${paymentMethod.paymentMethodName}"/></strong>?</c:if>
                            <c:if test="${not empty deliveryMethod.id}"> sposób dostawy <strong><c:out value="${deliveryMethod.deliveryMethodName}"/></strong>?</c:if>
                            </h3>
                            <form method="post">
                                <c:if test="${not empty shop.id}"> <input type="hidden" name="shopId" <c:out value="${shop.id}"/>></c:if>
                                <c:if test="${not empty brand.id}"> <input type="hidden" name="brandid" <c:out value="${brand.id}"/>></c:if>
                                <c:if test="${not empty productCategory.id}"> <input type="hidden" name="productCategoryId" <c:out value="${productCategory.id}"/>></c:if>
                                <c:if test="${not empty productSize.id}"> <input type="hidden" name="productSizeId" <c:out value="${productSize.id}"/>></c:if>
                                <c:if test="${not empty paymentMethod.id}"> <input type="hidden" name="paymentMethodId" <c:out value="${paymentMethod.id}"/>></c:if>
                                <c:if test="${not empty deliveryMethod.id}"> <input type="hidden" name="deliveryMethodId" <c:out value="${deliveryMethod.id}"/>></c:if>
                                <input class="button button-mini button-red button-3d" type="submit" value="Tak, usuń">
                                <button class="button button-mini button-blue button-3d" type="button" onClick="javascript:document.location.href='/admin/config'">Anuluj</button>
                            </form>

                        </div>

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

<!-- Go To Top
============================================= -->
<div id="gotoTop" class="icon-angle-up"></div>

<!-- JavaScripts
    <jsp:include page="../scripts.jsp"/>

</body>
</html>