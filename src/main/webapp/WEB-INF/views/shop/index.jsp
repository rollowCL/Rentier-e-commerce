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
            <h1>Sklep</h1>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">
                <div class="row">
                    <div class="filterForm">
                        <form:form class="myFormLeft" action="/search" method="post" modelAttribute="productSearch">
                            <form:label path="productName">Nazwa produktu</form:label>
                            <form:input path="productName"/>
                            <form:label path="brand">Marka</form:label>
                            <form:select path="brand">
                                <form:option value="0" label="wybierz..."/>
                                <form:options items="${brands}" itemValue="id" itemLabel="name"/>
                            </form:select>
                            <form:label path="productCategory">Kategoria</form:label>
                            <form:select path="productCategory">
                                <form:option value="0" label="wybierz..."/>
                                <form:options items="${productCategories}" itemValue="id" itemLabel="categoryName"/>
                            </form:select>
                            <form:label path="priceGrossFrom">Cena od</form:label>
                            <form:input path="priceGrossFrom" type="number" step="1" min="0"/>
                            <form:label path="priceGrossTo">Cena od</form:label>
                            <form:input path="priceGrossTo" type="number" step="1" min="0"/>
                            <form:errors path="priceGrossTo" cssClass="error"/>
                            <form:select path="sorting">
                                <form:option value="" label="wybierz..."/>
                                <form:option value="p" label="cena rosnąco"/>
                                <form:option value="pd" label="cena malejąco"/>
                                <form:option value="n" label="nazwa rosnąco"/>
                                <form:option value="nd" label="nazwa malejąco"/>
                            </form:select>
                            <button type="submit" class="button button-mini button-blue button-3d"
                                    value="Submit">Filtruj
                            </button>
                        </form:form>
                    </div>
                </div>
                <div id="shop" class="shop product grid-container clearfix" data-layout="fitRows">

                    <c:forEach items="${products}" var="product">

                        <div class="product clearfix">
                            <div class="product-image center">
                                <a href="/product?productId=<c:out value="${product.id}"/>">
                                    <img src="<c:out value="${product.imageFileName}"/>"
                                         alt="<c:out value="${product.productName}"/>">
                                </a>
                            </div>
                            <div class="product-desc center">
                                <div class="product-title"><h3><a href="/product?productId=<c:out
                                        value="${product.id}"/>"><c:out
                                        value="${product.productName}"/></a></h3></div>
                                <div class="product-price"><c:out
                                        value="${fn:replace(product.priceGross, '.', ',')} zł"/></div>
                                <div><img width="50%" height="50%"
                                          src="<c:out value="${product.brand.logoFileName}"/>"/></div>
                            </div>
                        </div>

                    </c:forEach>

                </div><!-- #shop end -->

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