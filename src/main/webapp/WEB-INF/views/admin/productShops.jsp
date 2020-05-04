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
                            <form:form class="myFormLeft" action="/admin/productShops/filterProductCategories" method="post" modelAttribute="productCategoryFilter">
                                <form:radiobutton path="id" value="0" label="Wszystkie"/>
                                <form:radiobuttons path="id" items="${productCategories}" itemLabel="categoryName" itemValue="id"/>
                                <button type="submit" class="button button-mini button-blue button-3d"
                                        value="Submit">Filtruj
                                </button>
                            </form:form>
                            <form class="myFormRight" action="${pageContext.request.contextPath}/admin/productShops/filterProductsName" method="post">
                                <label for="productNameSearch">Szukaj</label>
                                <input type="text" size="30" placeholder="podaj fragment nazwy produktu" minlength="3" name="productNameSearch" id="productNameSearch" maxlength="50"/>
                                <button type="submit" class="button button-mini button-blue button-3d"
                                        value="Submit">Szukaj
                                </button>
                            </form>
                            <a href="javascript:window.history.back()" class="button button-mini button-blue button-3d">Wstecz</a>
                        </div>


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
                                        <form action="${pageContext.request.contextPath}/admin/productShops/update" method="post">
                                            <input type="hidden" name="productShopId" value="${productShop.id}"/>
                                            <input type="number" step="1" min="1" max="999" value="${productShop.quantity}" name="newQuantity"/>
                                            <button type="submit"
                                                    class="button button-mini button-blue button-3d"
                                                    value="Submit">Aktualizuj
                                            </button>
                                            <a class="button button-mini button-red button-3d"
                                               href="${pageContext.request.contextPath}/admin/productShops/del?productShopId=${productShop.id}">Usuń
                                            </a>
                                            <a class="button button-mini button-blue button-3d"
                                               href="${pageContext.request.contextPath}/admin/productShops/form?productId=${productShop.product.id}">Nowy stan dla produktu
                                            </a>
                                        </form>
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

    <jsp:include page="../scripts.jsp"/>

</body>
</html>
