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
            <h1>Produkty</h1>
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
                        <div class="filterForm">
                            <form class="myFormLeft" action="/admin/products/filterProductsCategory" method="post">

                                <button type="submit" class="button button-mini button-blue button-3d"
                                        value="Submit">Filruj
                                </button>
                            </form>
                        </div>
                        <div class="filterForm">
                            <form class="myFormRight" action="/admin/products/filterProductsName" method="post">
                                <label for="productNameSearch">Szukaj</label>
                                <input type="text" minlength="3" name="productNameSearch" id="productNameSearch" maxlength="50"/>
                                <button type="submit" class="button button-mini button-blue button-3d"
                                        value="Submit">Szukaj
                                </button>
                            </form>
                        </div>
                        <a href="/admin/products/form" class="button button-mini button-blue button-3d">Dodaj nowy</a>
                        <table class="table">
                            <thead>
                            <th scope="col">Zdjęcie</th>
                            <th scope="col">Marka</th>
                            <th scope="col">Kategoria</th>
                            <th scope="col">Nazwa produktu</th>
                            <th scope="col">Opis produktu</th>
                            <th scope="col">Cena brutto</th>
                            <th scope="col">Dostępny na www</th>
                            <th scope="col">Aktywny</th>
                            <th scope="col">Akcje</th>
                            </thead>
                            <tbody>
                                <c:forEach items="${products}" var="product">
                                    <tr>
                                        <td>
                                            <c:if test="${not empty product.imageFileName}">
                                                <img height="90" width="380" src="<c:out value="${product.imageFileName}"/>"
                                                     alt="ProductImage"/>
                                            </c:if>
                                        </td>
                                        <td><c:out value="${product.brand.name}"/></td>
                                        <td><c:out value="${product.productCategory.categoryName}"/></td>
                                        <td><c:out value="${product.productName}"/></td>
                                        <td><c:out value="${product.productDesc}"/></td>
                                        <td><c:out value="${product.priceGross}"/></td>
                                        <td>
                                            <c:if test="${product.availableOnline}"><i
                                                    class="icon-line-square-check"></i></c:if>
                                            <c:if test="${!product.availableOnline}"><i
                                                    class="icon-line-square-cross"></i></c:if>
                                        </td>
                                        <td>
                                            <c:if test="${product.active}"><i
                                                    class="icon-line-square-check"></i></c:if>
                                            <c:if test="${!product.active}"><i
                                                    class="icon-line-square-cross"></i></c:if>
                                        </td>
                                        <td>
                                            <a class="button button-mini button-red button-3d"
                                               href="/admin/products/del?productId=${product.id}">Usuń
                                            </a>
                                            <a class="button button-mini button-blue button-3d"
                                               href="/admin/products/form?productId=${product.id}">Edytuj
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