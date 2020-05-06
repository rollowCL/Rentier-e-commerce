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
<%@ taglib prefix="for" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
            <h1>Sklep - widok produktu</h1>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="postcontent nobottommargin clearfix">

                    <div class="single-product">

                        <div class="product">

                            <div class="col_half">

                                <!-- Product Single - Gallery
                                ============================================= -->
                                <div class="product-single-image">
                                    <c:forEach items="${product.productImages}" var="productImage">
                                        <c:if test="${productImage.mainImage}">
                                            <img id="mainImage" src="<c:out value="${productImage.imageFileName}"/>"
                                                 alt="<c:out value="${product.productName}"/>">
                                        </c:if>
                                    </c:forEach>
                                </div><!-- Product Single - Gallery End -->
                                <div>
                                    <c:forEach items="${product.productImages}" var="productImage">
                                        <c:if test="${!productImage.mainImage}">
                                            <img class="product-multi-image" src="<c:out value="${productImage.imageFileName}"/>"
                                                 alt="<c:out value="${product.productName}"/>">
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </div>

                            <div class="col_half col_last product-desc">

                                <!-- Product Single - Price
                                ============================================= -->
                                <div class="product-price">Cena: <del></del><ins><c:out value="${fn:replace(product.priceGross, '.', ',')} zł"/></ins></div><!-- Product Single - Price End -->

                                <div class="clear"></div>
                                <div class="line"></div>

                                <!-- Product Single - Quantity & Cart Button
                                ============================================= -->
                                <form class="cart nobottommargin clearfix" method="post" action="${pageContext.request.contextPath}/cart/add">
                                    <div> Wybierz rozmiar:
                                        <select id="sizeSelect" name="productSizeId" class="sm-form-control">
                                            <option value="0">wybierz...</option>
                                            <for:forEach items="${productSizesWithMaxMap}" var="entry">
                                                <option value="${entry.key.id}" data-max="${entry.value}">${entry.key.sizeName}</option>
                                            </for:forEach>
                                        </select>
                                    </div>
                                    <div class="line"></div>
                                    <div class="quantity clearfix">
                                        <input type="button" value="-" class="minus">
                                        <input id="quantity" type="number" step="1" min="1" name="quantity" value="1" title="Qty" class="qty" size="2" />
                                        <input type="button" value="+" class="plus">
                                    </div>
                                    <input type="hidden" name="productId" value="${product.id}"/>
                                    <button id="addToCartButton" type="submit" class="add-to-cart button nomargin">Do koszyka</button>
                                </form><!-- Product Single - Quantity & Cart Button End -->

                                <div class="clear"></div>
                                <div class="line"></div>

                                <!-- Product Single - Short Description
                                ============================================= -->
                                <p>${product.productDesc}</p>

                                <!-- Product Single - Meta
                                ============================================= -->
                                <div class="card product-meta">
                                    <div class="card-body">
                                        <span itemprop="productID" class="sku_wrapper">Kod produktu: <span class="sku"><c:out value="${product.productText}"/></span></span><br/>
                                        <span class="posted_in">Kategoria: <a href="${pageContext.request.contextPath}/?categoryId=<c:out value="${product.productCategory.id}"/>" rel="tag"><c:out value="${product.productCategory.categoryName}"/></a>.</span>
                                    </div>
                                </div><!-- Product Single - Meta End -->

                            </div>

                            <div class="col_full nobottommargin">

                                <div class="tabs clearfix nobottommargin" id="tab-1">

                                    <ul class="tab-nav clearfix">
                                        <li><a href="#tab-stock"><i class="icon-align-justify2"></i><span class="d-none d-md-inline-block"> Dostępność w sklepach</span></a></li>
                                    </ul>

                                    <div class="tab-container col-md-9">

                                        <div class="tab-content clearfix" id="tab-stock">
                                            <table class="table table-striped table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th>Sklep</th>
                                                        <th>Rozmiar</th>
                                                        <th>Liczba dostępnych sztuk</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${productShops}" var="productShop">
                                                    <tr>
                                                        <td><c:out value="${productShop.shop.shopName}"/></td>
                                                        <td><c:out value="${productShop.productSize.sizeName}"/></td>
                                                        <td><c:out value="${productShop.quantity}"/></td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>

                                    </div>

                                </div>


                            </div>

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
