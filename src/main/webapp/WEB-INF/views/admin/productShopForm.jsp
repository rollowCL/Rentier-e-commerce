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
            <h1>Stan magazynowy dla produktu ${product.productName}</h1>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="row clearfix">

                    <div class="col-lg-12">
                        <div class="row">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th scope="col">Sklep</th>
                                    <th scope="col">Rozmiar</th>
                                    <th scope="col">Dostępna ilość</th>
                                    <th scope="col">Akcje</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${existingProductShops}" var="existingProductShop">
                                    <tr>
                                        <td><c:out value="${existingProductShop.shop.shopName}"/></td>
                                        <td align="center"><c:out value="${existingProductShop.productSize.sizeName}"/></td>
                                        <td align="center"><c:out value="${existingProductShop.quantity}"/></td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/admin/productShops/update"
                                                  method="post">
                                                <input type="hidden" name="productShopId"
                                                       value="${existingProductShop.id}"/>
                                                <input type="number" step="1" min="1" max="999"
                                                       value="${existingProductShop.quantity}" name="newQuantity"/>
                                                <button type="submit"
                                                        class="button button-mini button-blue button-3d"
                                                        value="Submit">Aktualizuj
                                                </button>
                                                <a class="button button-mini button-red button-3d"
                                                   href="${pageContext.request.contextPath}/admin/productShops/del?productShopId=${existingProductShop.id}">Usuń
                                                </a>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>

                            </table>
                            <div class="col-3">
                                <form:form class="row" method="post"
                                           id="productAddForm" modelAttribute="productShop">
                                <form:hidden path="id"/>
                                <form:hidden path="product"/>
                                <label for="shop">Wybierz sklep </label>
                                <form:select path="shop" class="formElement">
                                    <form:option value="0" label="wybierz..."/>
                                    <form:options items="${shops}" itemValue="id" itemLabel="shopName"/>
                                </form:select>
                            </div>
                            <div class="col-3">
                                <form:errors path="shop" cssClass="error"/>
                                <label for="productSize">Wybierz rozmiar </label>
                                <form:select path="productSize" class="formElement">
                                    <form:option value="0" label="wybierz..."/>
                                    <form:options items="${productSizes}" itemValue="id" itemLabel="sizeName"/>
                                </form:select>
                                <form:errors path="productSize" cssClass="error"/>
                            </div>
                            <div class="col-3">
                                <label for="quantity">Dostępna ilość</label>
                                <form:input type="number" step="1" min="1" max="999" path="quantity" id="quantity"
                                            class="formElement"
                                            maxlength="100"/>
                                <form:errors path="quantity" cssClass="error"/>
                            </div>
                            <div class="col-3">
                                <button type="submit"
                                        class="button button-mini button-blue button-3d"
                                        value="Submit">Dodaj
                                </button>
                            </div>
                        </div>
                        </form:form>

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
