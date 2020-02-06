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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
            <h1>Zamówienia</h1>
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
                            <form class="myFormRight" action="/admin/orders/filterOrderNumber" method="post">
                                <label for="orderNumberSearch">Szukaj</label>
                                <input type="text" size="15" placeholder="numer zamówienia" minlength="1" name="orderNumberSearch" id="orderNumberSearch" maxlength="50"/>
                                <button type="submit" class="button button-mini button-blue button-3d"
                                        value="Submit">Szukaj
                                </button>
                            </form>
                        </div>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th scope="col">Numer</th>
                                    <th scope="col">Status</th>
                                    <th scope="col">Data</th>
                                    <th scope="col">Metoda płatności</th>
                                    <th scope="col">Sposób dostawy</th>
                                    <th scope="col">Kosz dostawy</th>
                                    <th scope="col">Liczba produktów</th>
                                    <th scope="col">Koszt produktów</th>
                                    <th scope="col">Łączny koszt</th>
                                    <th scope="col">Akcje</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${orders}" var="order">
                                    <tr>
                                        <td><c:out value="${order.orderNumber}"/> </td>
                                        <td><c:out value="${order.orderStatus.orderStatusName}"/></td>
                                        <td>
                                            <fmt:parseDate value="${ order.orderDate }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                                            <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsedDateTime }" />
                                        </td>
                                        <td><c:out value="${order.paymentMethod.paymentMethodName}"/></td>
                                        <td><c:out value="${order.deliveryMethod.deliveryMethodName}"/></td>
                                        <td><c:out value="${fn:replace(order.deliveryMethodCost, '.', ',')} zł"/></td>
                                        <td><c:out value="${order.totalQuantity}"/></td>
                                        <td><c:out value="${fn:replace(order.totalValue, '.', ',')} zł"/></td>
                                        <c:set value="${order.totalValue + order.deliveryMethodCost}" var="totalValue"/>
                                        <td><c:out value="${fn:replace(totalValue, '.', ',')} zł"/></td>
                                        <td>
                                            <a class="button button-mini button-blue button-3d"
                                               href="/admin/orders/changeStatus?orderId=${order.id}">Zmień status
                                            </a>
                                            <a class="button button-mini button-blue button-3d"
                                               href="/admin/orders/details?orderId=${order.id}">Szczegóły
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


    <jsp:include page="../scripts.jsp"/>

</body>
</html>