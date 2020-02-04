<%--
Created by IntelliJ IDEA.
User: osint
Date: 2020-01-31
Time: 2:16 p.m.
To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
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
    <jsp:include page="../header.jsp"/>

    <!-- Page Title
============================================= -->
    <section id="page-title">

        <div class="container clearfix">
            <h1>Shop</h1>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="#">Home</a></li>
                <li class="breadcrumb-item active" aria-current="page">Shop</li>
            </ol>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="table-responsive">
                    <table class="table cart">
                        <thead>
                        <tr>
                            <th class="cart-product-remove">&nbsp;</th>
                            <th class="cart-product-thumbnail">&nbsp;</th>
                            <th class="cart-product-name">Nazwa produktu</th>
                            <th class="cart-product-name">Rozmiar</th>
                            <th class="cart-product-price">Cena</th>
                            <th class="cart-product-quantity">Ilość</th>
                            <th class="cart-product-subtotal">Wartość</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${sessionScope.cart.cartItems}" var="cartItem">
                            <tr class="cart_item">
                                <td class="cart-product-remove">
                                    <a href="/cart/remove?productId=${cartItem.product.id}&productSizeId=${cartItem.productSize.id}"
                                       class="remove" title="Usuń produkt z koszyka"><i class="icon-trash2"></i></a>
                                </td>

                                <td class="cart-product-thumbnail">
                                    <a href="/product?productId=${cartItem.product.id}"><img height="64" width="auto"
                                                                                             src="<c:out value="${cartItem.product.imageFileName}"/>"
                                                                                             alt="<c:out value="${cartItem.product.productName}"/>"></a>
                                </td>

                                <td class="cart-product-name">
                                    <a href="/product?productId=${cartItem.product.id}"><c:out
                                            value="${cartItem.product.productName}"/></a>
                                </td>

                                <td class="cart-product-name">
                                    <c:out value="${cartItem.productSize.sizeName}"/>
                                </td>

                                <td class="cart-product-price">
                                    <span class="amount"><c:out
                                            value="${fn:replace(cartItem.product.priceGross, '.', ',')} zł"/></span>
                                </td>

                                <td class="cart-product-quantity">
                                    <div class="quantity clearfix">
                                        <c:out value="${cartItem.quantity}"/>
                                    </div>
                                </td>
                                <c:set var="subtotal" value="${cartItem.product.priceGross * cartItem.quantity}"/>
                                <fmt:formatNumber var="substotalFormatted" value="${subtotal}" maxFractionDigits="2"/>
                                <td class="cart-product-subtotal">
                                    <span class="amount"><c:out
                                            value="${fn:replace(substotalFormatted, '.', ',')} zł"/></span>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr class="cart_item">
                            <td colspan="7">
                                <div class="row clearfix">
                                    <div class="col-lg-4 col-4 nopadding">

                                    </div>
                                    <div class="col-lg-8 col-8 nopadding">
<%--                                        <a href="#" class="button button-mini button-blue fright button-3d">Aktualizuj koszty</a>--%>
                                        <a href="/user/order/checkout" class="button button-mini button-blue fright button-3d">Zamówienie</a>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        </tbody>

                    </table>
                </div>
                <div class="row clearfix">
                    <div class="col-lg-6 clearfix"></div>
                    <div class="col-lg-6 clearfix">
                        <h4>Podsumowanie</h4>

                        <div class="table-responsive">
                            <table class="table cart">
                                <tbody>
                                <tr class="cart_item">
                                    <td class="cart-totals-tag">
                                        <strong>Razem</strong>
                                    </td>

                                    <td class="cart-product-name">
                                        <span class="amount">${sessionScope.cart.totalValue}</span>
                                    </td>
                                </tr>
<%--                                <tr class="cart_item">--%>
<%--                                    <td class="cart-totals-tag">--%>
<%--                                        <strong>Dostawa</strong>--%>
<%--                                    </td>--%>

<%--                                    <td class="cart-product-name">--%>
<%--                                        <span class="amount">0 zł</span>--%>
<%--                                    </td>--%>
<%--                                </tr>--%>
<%--                                <tr class="cart_item">--%>
<%--                                    <td class="cart-totals-tag">--%>
<%--                                        <strong>Razem</strong>--%>
<%--                                    </td>--%>

<%--                                    <td class="cart-product-name">--%>
<%--                                        <span class="amount color lead"><strong>$106.94</strong></span>--%>
<%--                                    </td>--%>
<%--                                </tr>--%>
                                </tbody>

                            </table>
                        </div>
                    </div>
                </div>
            </div>

        </div>

    </section><!-- #content end -->

    <!-- Footer
============================================= -->
    <jsp:include page="../footer.jsp"/>

</div><!-- #wrapper end -->


<!--  JavaScripts
============================================= -->
<jsp:include page="../scripts.jsp"/>

</body>
</html>