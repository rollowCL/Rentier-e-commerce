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
            <h1>Konfiguracja</h1>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="row clearfix">

                    <div class="col-lg-12">

                        <div class="tabs tabs-alt clearfix" id="tabs">

                            <ul class="tab-nav clearfix">
                                <li>
                                    <a href="#tab-admin-shops"><i class="icon-shop"></i> Sklepy</a>
                                </li>
                                <li>
                                    <a href="#tab-admin-brands"><i class="icon-archive1"></i> Marki</a>
                                </li>
                                <li>
                                    <a href="#tab-admin-categories"><i class="icon-folder2"></i> Kategorie</a>
                                </li>
                                <li>
                                    <a href="#tab-admin-sizes"><i class="icon-resize-full"></i> Rozmiary</a>
                                </li>
                                <li>
                                    <a href="#tab-admin-ship-methods"><i class="icon-luggage-cart"></i> Metody
                                        dostawy</a>
                                </li>
                                <li>
                                    <a href="#tab-admin-pay-methods"><i class="icon-credit"></i> Metody
                                        płatności</a>
                                </li>
                                <li>
                                    <a href="#tab-admin-order-statuses"><i class="icon-file3"></i> Statusy zamówień</a>
                                </li>

                            </ul>

                            <div class="tab-container">

                                <div class="tab-content clearfix" id="tab-admin-shops">
                                    <div class="card">
                                        <div class="card-body">

                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th scope="col">Nazwa sklepu</th>
                                                        <th scope="col">Kod sklepu</th>
                                                        <th scope="col">Kod pocztowy</th>
                                                        <th scope="col">Miasto</th>
                                                        <th scope="col">Ulica</th>
                                                        <th scope="col">Numer</th>
                                                        <th scope="col">Aktywny</th>
                                                        <th scope="col">Akcje</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${shops}" var="shop">
                                                    <tr>
                                                        <td><c:out value="${shop.shopName}"/></td>
                                                        <td><c:out value="${shop.shopCode}"/></td>
                                                        <td><c:out value="${shop.address.zipCode}"/></td>
                                                        <td><c:out value="${shop.address.city}"/></td>
                                                        <td><c:out value="${shop.address.street}"/></td>
                                                        <td><c:out value="${shop.address.streetNumber}"/></td>
                                                        <td>
                                                            <c:if test="${shop.active}"><i
                                                                    class="icon-line-square-check"></i></c:if>
                                                            <c:if test="${!shop.active}"><i
                                                                    class="icon-line-square-cross"></i></c:if>
                                                        </td>
                                                        <td>
                                                            <a class="button button-mini button-blue button-3d"
                                                               href="${pageContext.request.contextPath}/admin/config?shopId=${shop.id}">Edytuj
                                                            </a>
                                                            <a class="button button-mini button-blue button-3d"
                                                               href="${pageContext.request.contextPath}/admin/productShops?shopId=${shop.id}">Magazyn
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                            <div class="w-100 line divider-line"></div>
                                            <h4 class="card-title">
                                                <c:if test="${empty shop.id}">Dodaj nowy sklep</c:if>
                                                <c:if test="${not empty shop.id}">Edytuj sklep</c:if>
                                            </h4>
                                            <form:form class="row" method="post" action="${pageContext.request.contextPath}/admin/config/shop/add"
                                                       id="addForm" modelAttribute="shop">
                                                <form:hidden path="id"/>
                                                <input id="tab" type="hidden" value="${tab}"/>
                                                <div class="col-md-6 form-group">
                                                    <label for="shopName">Nazwa sklepu </label>
                                                    <form:input path="shopName" id="shopName" class="form-control"
                                                                maxlength="100"/>
                                                    <form:errors path="shopName" cssClass="error"/>
                                                </div>
                                                <div class="col-md-6 form-group">
                                                    <label for="shopCode">Kod sklepu </label>
                                                    <form:input path="shopCode" id="shopCode" class="form-control"
                                                                maxlength="100"/>
                                                    <form:errors path="shopCode" cssClass="error"/>
                                                </div>
                                                <div class="col-md-12 form-group"></div>
                                                <div class="col-md-2 form-group">
                                                    <label for="zipCode">Kod pocztowy </label>
                                                    <form:input path="address.zipCode" id="zipCode"
                                                                class="form-control" maxlength="6"/>
                                                    <form:errors path="address.zipCode" cssClass="error"/>
                                                </div>
                                                <div class="col-md-4 form-group">
                                                    <label for="city">Miasto </label>
                                                    <form:input path="address.city" id="city" class="form-control"
                                                                maxlength="50"/>
                                                    <form:errors path="address.city" cssClass="error"/>
                                                </div>
                                                <div class="col-md-4 form-group">
                                                    <label for="street">Ulica </label>
                                                    <form:input path="address.street" id="street" class="form-control"
                                                                maxlength="50"/>
                                                    <form:errors path="address.street" cssClass="error"/>
                                                </div>
                                                <div class="col-md-2 form-group">
                                                    <label for="streetNumber">Numer </label>
                                                    <form:input path="address.streetNumber" id="streetNumber"
                                                                class="form-control" maxlength="10"/>
                                                    <form:errors path="address.streetNumber" cssClass="error"/>
                                                </div>
                                                <div class="col-md-4 form-group">
                                                    <label for="phone">Telefon </label>
                                                    <form:input path="phone" id="phone" class="form-control"
                                                                maxlength="9"/>
                                                    <form:errors path="phone" cssClass="error"/>
                                                </div>
                                                <div class="col-md-4 form-group">
                                                    <label for="email">Email </label>
                                                    <form:input path="email" id="email" class="form-control"
                                                                maxlength="50"/>
                                                    <form:errors path="email" cssClass="error"/>
                                                </div>
                                                <div class="col-md-1 form-group">
                                                    <label for="active">Aktywny </label>
                                                    <form:checkbox path="active" checked="checked" id="active" class="sm-form-control"/>
                                                    <form:errors path="active" cssClass="error"/>
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

                                <div class="tab-content clearfix" id="tab-admin-brands">
                                    <div class="card">

                                        <div class="card-body">

                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th scope="col">Logo</th>
                                                        <th scope="col">Marka</th>
                                                        <th scope="col">Email</th>
                                                        <th scope="col">Aktywny</th>
                                                        <th scope="col">Akcje</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${brands}" var="brand">
                                                    <tr>
                                                        <td>
                                                            <c:if test="${not empty brand.logoFileName}">
                                                            <img height="90" width="380" src="<c:url value="${brand.logoFileName}"/>"
                                                                 alt="BrandLogo"/>
                                                            </c:if>
                                                        </td>
                                                        <td><c:out value="${brand.name}"/></td>
                                                        <td><c:out value="${brand.email}"/></td>
                                                        <td>
                                                            <c:if test="${brand.active}"><i
                                                                    class="icon-line-square-check"></i></c:if>
                                                            <c:if test="${!brand.active}"><i
                                                                    class="icon-line-square-cross"></i></c:if>
                                                        </td>
                                                        <td>
                                                            <a class="button button-mini button-red button-3d"
                                                                    href="${pageContext.request.contextPath}/admin/config/del?brandId=${brand.id}">Usuń
                                                            </a>
                                                            <a class="button button-mini button-blue button-3d"
                                                                    href="${pageContext.request.contextPath}/admin/config?brandId=${brand.id}">Edytuj
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                            <div class="w-100 line divider-line"></div>
                                            <h4 class="card-title">
                                                <c:if test="${empty brand.id}">Dodaj nową markę</c:if>
                                                <c:if test="${not empty brand.id}">Edytuj markę</c:if>
                                            </h4>
                                            <form:form class="row" method="post" action="${pageContext.request.contextPath}/admin/config/brand/add"
                                                       id="brandAddForm" modelAttribute="brand" enctype="multipart/form-data">
                                                <form:hidden path="id"/>
                                                <div class="col-md-6 form-group">
                                                    <label for="name">Nazwa </label>
                                                    <form:input path="name" id="name" class="form-control"
                                                                maxlength="100"/>
                                                    <form:errors path="name" cssClass="error"/>
                                                </div>
                                                <div class="col-md-4 form-group">
                                                    <label for="email">Email </label>
                                                    <form:input path="email" id="email" class="form-control"
                                                                maxlength="50"/>
                                                    <form:errors path="email" cssClass="error"/>
                                                </div>
                                                <div class="col-md-1 form-group">
                                                    <label for="active">Aktywny </label>
                                                    <form:checkbox path="active" id="active" class="sm-form-control"/>
                                                    <form:errors path="active" cssClass="error"/>
                                                </div>
                                                <div class="col-md-6 form-group">
                                                    <label for="file">Plik logo <small></small></label>
                                                    <input accept="image/png, image/jpeg" type="file" id="file"
                                                           name="file" class="form-control required" />
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

                                <div class="tab-content clearfix" id="tab-admin-categories">
                                    <div class="card-body">
                                        <div class="col-md-7">
                                                <table class="table table-bordered">
                                                    <thead>
                                                        <tr>
                                                            <th scope="col">Nazwa</th>
                                                            <th scope="col">Pozycja</th>
                                                            <th scope="col">Aktywny</th>
                                                            <th scope="col">Akcje</th>
                                                        </tr>
                                                    </thead>
                                                <tbody>
                                                <c:forEach items="${productCategories}" var="productCategory">
                                                    <tr>
                                                        <td><c:out value="${productCategory.categoryName}"/></td>
                                                        <td><c:out value="${productCategory.categoryOrder}"/></td>
                                                        <td>
                                                            <c:if test="${productCategory.active}"><i
                                                                    class="icon-line-square-check"></i></c:if>
                                                            <c:if test="${!productCategory.active}"><i
                                                                    class="icon-line-square-cross"></i></c:if>
                                                        </td>
                                                        <td>
                                                            <a class="button button-mini button-red button-3d"
                                                               href="${pageContext.request.contextPath}/admin/config/del?productCategoryId=${productCategory.id}">Usuń
                                                            </a>
                                                            <a class="button button-mini button-blue button-3d"
                                                               href="${pageContext.request.contextPath}/admin/config?productCategoryId=${productCategory.id}">Edytuj
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                            <div class="w-100 line divider-line"></div>
                                            <h4 class="card-title">
                                                <c:if test="${empty brand.id}">Dodaj nową kategorię</c:if>
                                                <c:if test="${not empty brand.id}">Edytuj kategorię</c:if>
                                            </h4>
                                            <form:form class="row" method="post" action="${pageContext.request.contextPath}/admin/config/productCategory/add"
                                                       id="categoryAddForm" modelAttribute="productCategory">
                                                <form:hidden path="id"/>
                                                <div class="col-md-6 form-group">
                                                    <label for="categoryName">Nazwa </label>
                                                    <form:input path="categoryName" id="categoryName" class="form-control"
                                                                maxlength="100"/>
                                                    <form:errors path="categoryName" cssClass="error"/>
                                                </div>
                                                <div class="col-md-4 form-group">
                                                    <label for="categoryOrder">Pozycja </label>
                                                    <form:input path="categoryOrder" id="categoryOrder" class="form-control"
                                                                maxlength="100"/>
                                                    <form:errors path="categoryOrder" cssClass="error"/>
                                                </div>
                                                <div class="col-md-1 form-group">
                                                    <label for="active">Aktywny </label>
                                                    <form:checkbox path="active" checked="checked" id="active" class="sm-form-control"/>
                                                    <form:errors path="active" cssClass="error"/>
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

                                <div class="tab-content clearfix" id="tab-admin-sizes">
                                    <div class="card-body">

                                        <div class="col-md-7">
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th scope="col">Nazwa</th>
                                                        <th scope="col">Dotyczy kategorii</th>
                                                        <th scope="col">Aktywny</th>
                                                        <th scope="col">Akcje</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${productSizes}" var="productSize">
                                                    <tr>
                                                        <td><c:out value="${productSize.sizeName}"/></td>
                                                        <td><c:out value="${productSize.productCategory.categoryName}"/></td>
                                                        <td>
                                                            <c:if test="${productSize.active}"><i
                                                                    class="icon-line-square-check"></i></c:if>
                                                            <c:if test="${!productSize.active}"><i
                                                                    class="icon-line-square-cross"></i></c:if>
                                                        </td>
                                                        <td>
                                                            <a class="button button-mini button-red button-3d"
                                                               href="${pageContext.request.contextPath}/admin/config/del?productSizeId=${productSize.id}">Usuń
                                                            </a>
                                                            <a class="button button-mini button-blue button-3d"
                                                               href="${pageContext.request.contextPath}/admin/config?productSizeId=${productSize.id}">Edytuj
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                            <div class="w-100 line divider-line"></div>
                                            <h4 class="card-title">
                                                <c:if test="${empty productSize.id}">Dodaj nowy rozmiar</c:if>
                                                <c:if test="${not empty productSize.id}">Edytuj rozmiar</c:if>
                                            </h4>
                                            <form:form class="row" method="post" action="${pageContext.request.contextPath}/admin/config/productSize/add"
                                                       id="sizeAddForm" modelAttribute="productSize">
                                                <form:hidden path="id"/>
                                                <div class="col-md-6 form-group">
                                                    <label for="sizeName">Nazwa </label>
                                                    <form:input path="sizeName" id="sizeName" class="form-control"
                                                                maxlength="50"/>
                                                    <form:errors path="sizeName" cssClass="error"/>
                                                </div>
                                                <div class="col-md-6 form-group">
                                                    <label for="active">Wybierz kategorię </label>
                                                    <form:select path="productCategory" class="form-control">
                                                        <form:option value="0" label="wybierz..."/>
                                                        <form:options items="${productCategories}" itemValue="id" itemLabel="categoryName"/>
                                                    </form:select>
                                                    <form:errors path="productCategory" cssClass="error"/>
                                                </div>
                                                <div class="col-md-1 form-group">
                                                    <label for="active">Aktywny </label>
                                                    <form:checkbox path="active" id="active" class="sm-form-control"/>
                                                    <form:errors path="active" cssClass="error"/>
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

                                <div class="tab-content clearfix" id="tab-admin-ship-methods">
                                    <div class="card-body">

                                        <div class="col-md-7">
                                            <table class="table table-bordered">
                                                <thead>
                                                <tr>
                                                    <th scope="col">Nazwa</th>
                                                    <th scope="col">Koszt</th>
                                                    <th scope="col">Aktywny</th>
                                                    <th scope="col">Akcje</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${deliveryMethods}" var="deliveryMethod">
                                                    <tr>
                                                        <td><c:out value="${deliveryMethod.deliveryMethodName}"/></td>
                                                        <td><c:out value="${fn:replace(deliveryMethod.deliveryMethodCost, '.', ',')}"/> zł</td>
                                                        <td>
                                                            <c:if test="${deliveryMethod.active}"><i
                                                                    class="icon-line-square-check"></i></c:if>
                                                            <c:if test="${!deliveryMethod.active}"><i
                                                                    class="icon-line-square-cross"></i></c:if>
                                                        </td>
                                                        <td>
                                                            <a class="button button-mini button-blue button-3d"
                                                               href="${pageContext.request.contextPath}/admin/config?deliveryMethodId=${deliveryMethod.id}">Edytuj
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                            <div class="w-100 line divider-line"></div>
                                            <h4 class="card-title">
                                                <c:if test="${empty deliveryMethod.id}">Dodaj nowy sposób dostawy</c:if>
                                                <c:if test="${not empty deliveryMethod.id}">Edytuj sposób dostawy</c:if>
                                            </h4>
                                            <form:form class="row" method="post" action="${pageContext.request.contextPath}/admin/config/deliveryMethod/add"
                                                       id="deliveryMethodAddForm" modelAttribute="deliveryMethod">
                                                <form:hidden path="id"/>
                                                <div class="col-md-6 form-group">
                                                    <label for="deliveryMethodName">Nazwa </label>
                                                    <form:input path="deliveryMethodName" id="deliveryMethodName" class="form-control"
                                                                maxlength="50"/>
                                                    <form:errors path="deliveryMethodName" cssClass="error"/>
                                                </div>
                                                <div class="col-md-2 form-group">
                                                    <label for="deliveryMethodCost">Koszt </label>
                                                    <form:input path="deliveryMethodCost" id="deliveryMethodCost" class="form-control"
                                                                maxlength="50"/>
                                                    <form:errors path="deliveryMethodCost" cssClass="error"/>
                                                </div>
                                                <div class="col-md-1 form-group">
                                                    <label for="active">Aktywny </label>
                                                    <form:checkbox path="active" id="active" class="sm-form-control"/>
                                                    <form:errors path="active" cssClass="error"/>
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

                                <div class="tab-content clearfix" id="tab-admin-pay-methods">
                                    <div class="card-body">

                                        <div class="col-md-7">
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th scope="col">Nazwa</th>
                                                        <th scope="col">Aktywny</th>
                                                        <th scope="col">Akcje</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${paymentMethods}" var="paymentMethod">
                                                    <tr>
                                                        <td><c:out value="${paymentMethod.paymentMethodName}"/></td>
                                                        <td>
                                                            <c:if test="${paymentMethod.active}"><i
                                                                    class="icon-line-square-check"></i></c:if>
                                                            <c:if test="${!paymentMethod.active}"><i
                                                                    class="icon-line-square-cross"></i></c:if>
                                                        </td>
                                                        <td>
                                                            <a class="button button-mini button-blue button-3d"
                                                               href="${pageContext.request.contextPath}/admin/config?paymentMethodId=${paymentMethod.id}">Edytuj
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                            <div class="w-100 line divider-line"></div>
                                            <h4 class="card-title">
                                                <c:if test="${empty paymentMethod.id}">Dodaj nową metodę płatności</c:if>
                                                <c:if test="${not empty paymentMethod.id}">Edytuj metodę płatności</c:if>
                                            </h4>
                                            <form:form class="row" method="post" action="${pageContext.request.contextPath}/admin/config/paymentMethod/add"
                                                       id="paymentMethodAddForm" modelAttribute="paymentMethod">
                                                <form:hidden path="id"/>
                                                <div class="col-md-6 form-group">
                                                    <label for="paymentMethodName">Nazwa </label>
                                                    <form:input path="paymentMethodName" id="paymentMethodName" class="form-control"
                                                                maxlength="50"/>
                                                    <form:errors path="paymentMethodName" cssClass="error"/>
                                                </div>
                                                <div class="col-md-1 form-group">
                                                    <label for="active">Aktywny </label>
                                                    <form:checkbox path="active" id="active" class="sm-form-control"/>
                                                    <form:errors path="active" cssClass="error"/>
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



                                <div class="tab-content clearfix" id="tab-admin-order-statuses">
                                    <div class="card-body">

                                        <div class="col-md-7">
                                            <table class="table table-bordered">
                                                <thead>
                                                <tr>
                                                    <th scope="col">Nazwa</th>
                                                    <th scope="col">Dotyczy metody dostawy</th>
                                                    <th scope="col">Aktywny</th>
                                                    <th scope="col">Akcje</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${orderStatuses}" var="orderStatus">
                                                    <tr>
                                                        <td><c:out value="${orderStatus.orderStatusName}"/></td>
                                                        <td><c:out value="${orderStatus.deliveryMethod.deliveryMethodName}"/> </td>
                                                        <td>
                                                            <c:if test="${orderStatus.active}"><i
                                                                    class="icon-line-square-check"></i></c:if>
                                                            <c:if test="${!orderStatus.active}"><i
                                                                    class="icon-line-square-cross"></i></c:if>
                                                        </td>
                                                        <td>
                                                            <a class="button button-mini button-blue button-3d"
                                                               href="${pageContext.request.contextPath}/admin/config?orderStatusId=${orderStatus.id}">Edytuj
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                            <div class="w-100 line divider-line"></div>
                                            <h4 class="card-title">
                                                <c:if test="${empty orderStatus.id}">Dodaj nowy status</c:if>
                                                <c:if test="${not empty orderStatus.id}">Edytuj status</c:if>
                                            </h4>
                                            <form:form class="row" method="post" action="${pageContext.request.contextPath}/admin/config/orderStatus/add"
                                                       id="orderStatusAddForm" modelAttribute="orderStatus">
                                                <form:hidden path="id"/>
                                                <div class="col-md-6 form-group">
                                                    <label for="orderStatusName">Nazwa </label>
                                                    <form:input path="orderStatusName" id="orderStatusName" class="form-control"
                                                                maxlength="50"/>
                                                    <form:errors path="orderStatusName" cssClass="error"/>
                                                </div>
                                                <div class="col-md-4 form-group">
                                                    <label for="deliveryMethod">Sposób dostawy </label>
                                                    <form:select path="deliveryMethod" class="form-control">
                                                        <form:option value="0" label="wybierz..."/>
                                                        <form:options items="${deliveryMethods}" itemValue="id" itemLabel="deliveryMethodName"/>
                                                    </form:select>
                                                    <form:errors path="deliveryMethod" cssClass="error"/>
                                                </div>
                                                <div class="col-md-1 form-group">
                                                    <label for="active">Aktywny </label>
                                                    <form:checkbox path="active" id="active" class="sm-form-control"/>
                                                    <form:errors path="active" cssClass="error"/>
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

                        </div>

                    </div>


                    <div class="w-100 line d-block d-md-none"></div>
                    <div id="returnToTag">${sessionScope.returnToTag}</div>


                </div>

            </div>

        </div>

    </section><!-- #content end -->

    <!-- Footer
============================================= -->
    <jsp:include page="footer.jsp"/>

</div><!-- #wrapper end -->

<jsp:include page="../scripts.jsp"/>
<script src='<spring:url value="/js/configReturn.js"/>'></script>
</body>
</html>
