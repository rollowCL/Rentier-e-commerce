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
            <h1>Sklep</h1>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="clear"></div>

                <div class="row clearfix">
                    <div class="col-md-6">
                        <h3>Adres na fakturze</h3>
                        <form:form id="orderForm" class="row nobottommargin" modelAttribute="order">

                            <form:label path="billAddress.zipCode">Kod pocztowy</form:label>
                            <form:input path="billAddress.zipCode" id="billAddress.zipCode" class="form-control"
                                        maxlength="6"/>
                            <form:errors path="billAddress.zipCode" cssClass="error"/>
                            <br/>
                            <form:label path="billAddress.city">Miasto</form:label>
                            <form:input path="billAddress.city" id="billAddress.city" class="form-control"
                                        maxlength="50"/>
                            <form:errors path="billAddress.city" cssClass="error"/>
                            <br/>
                            <form:label path="billAddress.street">Ulica</form:label>
                            <form:input path="billAddress.street" id="billAddress.street" class="form-control"
                                        maxlength="50"/>
                            <form:errors path="billAddress.street" cssClass="error"/>
                            <br/>
                            <form:label path="billAddress.streetNumber">Numer</form:label>
                            <form:input path="billAddress.streetNumber" id="billAddress.streetNumber" class="form-control"
                                        maxlength="50"/>
                            <form:errors path="billAddress.streetNumber" cssClass="error"/>
                    </div>
                    <div class="col-md-6">
                        <h3 class="">Adres Dostawy</h3>
                            <form:label path="shipAddress.zipCode">Kod pocztowy</form:label>
                            <form:input path="shipAddress.zipCode" id="shipAddress.zipCode" class="form-control"
                                        maxlength="6"/>
                            <form:errors path="shipAddress.zipCode" cssClass="error"/>
                            <br/>
                            <form:label path="shipAddress.city">Miasto</form:label>
                            <form:input path="shipAddress.city" id="shipAddress.city" class="form-control"
                                        maxlength="50"/>
                            <form:errors path="shipAddress.city" cssClass="error"/>
                            <br/>
                            <form:label path="shipAddress.street">Ulica</form:label>
                            <form:input path="shipAddress.street" id="shipAddress.street" class="form-control"
                                        maxlength="50"/>
                            <form:errors path="shipAddress.street" cssClass="error"/>
                            <br/>
                            <form:label path="shipAddress.streetNumber">Numer</form:label>
                            <form:input path="shipAddress.streetNumber" id="shipAddress.streetNumber" class="form-control"
                                        maxlength="50"/>
                            <form:errors path="shipAddress.streetNumber" cssClass="error"/>
                    </div>
                    <div class="w-100 bottommargin"></div>
                    <div class="col-lg-6">
                        <h4 class="card-title">Wybierz sposób dostawy</h4>
                            <form:select path="deliveryMethod" class="form-control">
                                <form:option value="0" label="wybierz..."/>
                                <form:options items="${deliveryMethods}" itemValue="id"
                                              itemLabel="deliveryMethodNameAndCost"/>
                            </form:select>
                            <form:errors path="deliveryMethod" cssClass="error"/>
                            <h4 class="card-title">Wybierz sposób płatności</h4>
                            <form:select path="paymentMethod" class="form-control">
                                <form:option value="0" label="wybierz..."/>
                                <form:options items="${paymentMethods}" itemValue="id"
                                              itemLabel="paymentMethodName"/>
                            </form:select>
                            <form:errors path="paymentMethod" cssClass="error"/>

                            <h4 class="card-title">Wybierz sklep</h4>
                            <form:select path="pickupShop" class="form-control">
                                <form:option value="0" label="wybierz..."/>
                                <form:options items="${shops}" itemValue="id"
                                              itemLabel="shopName"/>
                            </form:select>
                            <form:errors path="pickupShop" cssClass="error"/>
                    </div>
                    <div class="col-lg-6">
                        <h4>Podsumowanie</h4>
                        <div class="table-responsive">
                            <table class="table cart">
                                <tbody>
                                <tr class="cart_item">
                                    <td class="cart-totals-tag">
                                        <strong>Wartość produktów</strong>
                                    </td>

                                    <td class="cart-product-name">
                                        <span class="amount">${sessionScope.cart.totalValue}</span>
                                    </td>
                                </tr>
                                <tr class="cart_item">
                                    <td class="cart-totals-tag">
                                        <strong>Koszt dostawy</strong>
                                    </td>

                                    <td class="cart-product-name">
                                        <span class="amount">0 zł</span>
                                    </td>
                                </tr>
                                <tr class="cart_item">
                                    <td class="cart-totals-tag">
                                        <strong>Razem</strong>
                                    </td>

                                    <td class="cart-product-name">
                                        <span class="amount color lead"><strong>${sessionScope.cart.totalValue}</strong></span>
                                    </td>
                                </tr>
                                </tbody>

                            </table>
                        </div>
                        <button type="submit"
                                       class="button button-mini button-blue button-3d"
                                       value="Submit">Zamawiam i płacę
                        </button>
                        <button class="button button-mini button-blue button-3d" type="button" onClick="javascript:document.location.href='/admin/config'">Wstecz</button>
                    </form:form>
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