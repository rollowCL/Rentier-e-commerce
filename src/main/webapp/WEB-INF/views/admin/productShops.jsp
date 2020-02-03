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
            <h1>Stan magazynowy</h1>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="#">Home</a></li>
                <li class="breadcrumb-item active" aria-current="page">Produkty</li>
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
                        <a href="/admin/productShops/form" class="button button-mini button-blue button-3d">Dodaj
                            nowy</a>

                        <table class="table">
                            <thead>
                            <tr>
                                <th scope="col">Sklep</th>
                                <th scope="col">Produkt</th>
                                <th scope="col">Rozmiar</th>
                                <th scope="col">Dostępna ilość</th>
                                <th scope="col">Akcje</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${productShops}" var="productShop">
                                <tr>
                                    <td><c:out value="${productShop.shop.shopName}"/></td>
                                    <td><c:out value="${productShop.product.productName}"/></td>
                                    <td><c:out value="${productShop.productSize.sizeName}"/></td>
                                    <td><c:out value="${productShop.quantity}"/></td>
                                    <td>
                                        <a class="button button-mini button-red button-3d"
                                           href="/admin/productShops/del?productShopId=${productShop.id}">Usuń
                                        </a>
                                        <a class="button button-mini button-blue button-3d"
                                           href="/admin/productShops/form?productShopId=${productShop.id}">Edytuj
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>

                        </table>

                    </div>
                </div>

            </div>

        </div>

    </section><!-- #content end -->

    <!-- Footer
============================================= -->
    <jsp:include page="footer.jsp"/>

</div><!-- #wrapper end -->

<!-- JavaScripts
    <jsp:include page="../scripts.jsp"/>

</body>
</html>