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
                                   id="productAddForm" modelAttribute="product" enctype="multipart/form-data">
                            <form:hidden path="id"/>
                            <form:hidden path="createdDate"/>
                            <form:hidden path="updatedDate"/>
                            <div class="col-md-6 form-group">
                                <label for="active">Wybierz kategorię </label>
                                <form:select path="productCategory" class="formElement">
                                    <form:option value="0" label="wybierz..."/>
                                    <form:options items="${productCategories}" itemValue="id" itemLabel="categoryName"/>
                                </form:select>
                                <form:errors path="productCategory" cssClass="error"/>
                            </div>
                            <div class="col-md-6 form-group">
                                <label for="active">Wybierz markę </label>
                                <form:select path="brand" class="formElement">
                                    <form:option value="0" label="wybierz..."/>
                                    <form:options items="${brands}" itemValue="id" itemLabel="name"/>
                                </form:select>
                                <form:errors path="brand" cssClass="error"/>
                            </div>
                            <div class="col-md-6 form-group">
                                <label for="productName">Nazwa produktu</label>
                                <form:input path="productName" id="productName" class="form-control"
                                            maxlength="50"/>
                                <form:errors path="productName" cssClass="error"/>
                            </div>
                            <div class="col-md-6 form-group">
                                <label for="fileName">Plik zdjęcia </label>
                                <input accept="image/png, image/jpeg" type="file" id="fileName"
                                       name="fileName" class="form-control required" />
                            </div>
                            <div class="col-md-12 form-group">
                                <label for="productDesc">Opis produktu </label>
                                <form:textarea cols="20" rows="5" path="productDesc" id="productDesc" class="form-control"
                                            maxlength="1000"/>
                                <form:errors path="productDesc" cssClass="error"/>
                            </div>
                            </div>
                            <div class="col-md-3 form-group">
                                <label for="priceGross">Cena brutto</label>
                                <form:input type="number" min="0.01" step="0.01" path="priceGross" id="priceGross" class="form-control"
                                            maxlength="100"/>
                                <form:errors path="priceGross" cssClass="error"/>
                            </div>
                            <div class="col-md-1 form-group">
                                <label for="active">Aktywny </label>
                                <form:checkbox path="active" id="active" class="form-control"/>
                                <form:errors path="active" cssClass="error"/>
                            </div>
                            <div class="col-md-1 form-group">
                                <label for="availableOnline">Dostępny na www </label>
                                <form:checkbox path="availableOnline" id="availableOnline" class="form-control"/>
                                <form:errors path="availableOnline" cssClass="error"/>
                            </div>
                            <div class="col-12">
                                <button type="submit"
                                        class="button button-mini button-blue button-3d"
                                        value="Submit">Zapisz
                                </button>
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