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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags/util" prefix="util" %>
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
            <h1>Szczegóły zamówienia ${order.orderNumber}</h1>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="row clearfix">

                    <div class="col-lg-12">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th scope="col">Kategoria</th>
                                <th scope="col">Produkt</th>
                                <th scope="col">Rozmiar</th>
                                <th scope="col">Cena brutto</th>
                                <th scope="col">Ilość</th>
                                <th scope="col">Wartość brutto</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${order.orderDetails}" var="detail">
                                <tr>
                                    <td><c:out value="${detail.productCategoryName}"/></td>
                                    <td><c:out value="${detail.productName}"/></td>
                                    <td><c:out value="${detail.productSizeName}"/></td>
                                    <td class="text-right"><c:out value="${fn:replace(detail.priceGross, '.', ',')} zł"/></td>
                                    <td class="text-right"><c:out value="${detail.quantity}"/></td>
                                    <td class="text-right"><c:out value="${fn:replace(detail.valueGross, '.', ',')} zł"/></td>
                                </tr>
                            </c:forEach>
                                <tr>
                                    <td class="text-right" colspan="4"><strong>Razem</strong></td>
                                    <td class="text-right"><strong><c:out value="${order.totalQuantity}"/></strong></td>
                                    <td class="text-right"><strong><c:out value="${fn:replace(order.totalValue, '.', ',')} zł"/></strong></td>
                                </tr>
                            </tbody>
                        </table>
                        <a href="javascript:window.history.back()" class="button fright button-mini button-blue button-3d">Wstecz</a>
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