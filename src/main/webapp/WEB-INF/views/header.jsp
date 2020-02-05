<%--
  Created by IntelliJ IDEA.
  User: osint
  Date: 2020-01-26
  Time: 12:47 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header id="header">

    <div id="header-wrap">

        <div class="container clearfix">

            <div id="primary-menu-trigger"><i class="icon-reorder"></i></div>

            <!-- Logo
            ============================================= -->
            <div id="logo">
                <div class="myLogo">
                    <a href="/" class="standard-logo"><img src="/images/logo.png" alt="Rentier Logo"></a>
                </div>

            </div><!-- #logo end -->

            <!-- Primary Navigation
            ============================================= -->
            <nav id="primary-menu" class="style-3">

                <ul>

                    <c:forEach items="${productCategories}" var="productCategory">
                        <li>
                            <a href="/?categoryId=<c:out value="${productCategory.id}"/>">
                                <div><c:out value="${productCategory.categoryName}"/></div>
                            </a>
                        </li>
                    </c:forEach>


                </ul>

                <div id="top-cart">
                    <a href="/cart">
                        <i class="icon-shopping-cart"></i>
                        <span>
                            <c:choose>
                                <c:when test="${not empty sessionScope.cart.totalQuantity}">
                                    <c:out value="${sessionScope.cart.totalQuantity}"/>
                                </c:when>
                                <c:when test="${empty sessionScope.cart.totalQuantity}">
                                    0
                                </c:when>
                            </c:choose>
                        </span></a>
                </div><!-- #top-cart end -->
                <div id="top-search">
                    <c:if test="${loggedId == null}"><a href="/login"><i class="icon-user-alt"></i></a></c:if>
                    <c:if test="${loggedId != null}"><a href="/logout"><i class="icon-user-slash"></i></a></c:if>
                </div>

            </nav><!-- #primary-menu end -->

        </div>

    </div>

</header><!-- #header end -->
