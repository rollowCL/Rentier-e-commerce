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
                        <form:form class="row" method="post"
                                   id="productAddForm" modelAttribute="productShop">
                            <form:hidden path="id"/>
                            <div class="col-md-12 form-group">
                                <label for="shop">Wybierz sklep </label>
                                <form:select path="shop" class="formElement">
                                    <form:option value="0" label="wybierz..."/>
                                    <form:options items="${shops}" itemValue="id" itemLabel="shopName"/>
                                </form:select>
                                <form:errors path="shop" cssClass="error"/>
                            </div>
                            <div class="col-md-4 form-group">
                                <label for="product">Wybierz produkt </label>
                                <form:select path="product" class="formElement">
                                    <form:option value="0" label="wybierz..."/>
                                    <form:options items="${products}" itemValue="id" itemLabel="productName"/>
                                </form:select>
                                <form:errors path="product" cssClass="error"/>
                            </div>
                            <div class="col-md-4 form-group">
                                <label for="productSize">Wybierz rozmiar </label>
                                <form:select path="productSize" class="formElement">
                                    <form:option value="0" label="wybierz..."/>
                                    <form:options items="${productSizes}" itemValue="id" itemLabel="sizeName"/>
                                </form:select>
                                <form:errors path="productSize" cssClass="error"/>
                            </div>

                            </div>
                            <div class="col-md-3 form-group">
                                <label for="quantity">Dostępna ilość</label>
                                <form:input type="number" min="1" step="1" path="quantity" id="quantity" class="form-control"
                                            maxlength="100"/>
                                <form:errors path="quantity" cssClass="error"/>
                            </div>
                            <div class="col-12">
                                <button type="submit"
                                        class="button button-mini button-blue button-3d"
                                        value="Submit">Zapisz
                                </button>
                                <button class="button button-mini button-blue button-3d" type="button" onClick="javascript:document.location.href='/admin/config'">Anuluj</button>

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

<!-- JavaScripts
    <jsp:include page="../scripts.jsp"/>

</body>
</html>