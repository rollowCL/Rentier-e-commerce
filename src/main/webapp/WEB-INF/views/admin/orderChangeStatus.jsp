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
            <h1>Zmiana statusu zamówienia ${order.orderNumber}</h1>
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
                            <div class="col-4">
                            </div>
                            <div class="col-4">
                                <form action="/admin/orders/changeStatus" method="post">
                                    <input type="hidden" value="${order.id}" name="orderId"/>
                                    <select name="orderStatusId">
                                        <c:forEach items="${orderStatuses}" var="orderStatus">
                                            <option value="${orderStatus.id}"
                                                    <c:if test="${order.orderStatus == orderStatus}">selected</c:if>>${orderStatus.orderStatusName}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="button button-mini button-blue button-3d"
                                            value="Submit">Zapisz
                                    </button>
                                    <a class="button button-mini button-blue button-3d" href="javascript: window.history.go(-1)">Anuluj</a>
                                </form>
                            </div>
                        </div>
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