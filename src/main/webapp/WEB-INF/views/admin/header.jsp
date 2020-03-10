<%--
  Created by IntelliJ IDEA.
  User: osint
  Date: 2020-01-26
  Time: 12:47 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<header id="header">

    <div id="header-wrap">

        <div class="container clearfix">

            <div id="primary-menu-trigger"><i class="icon-reorder"></i></div>

            <!-- Logo
            ============================================= -->
            <div id="logo">
                <div class="myLogo">
                    <a href="${pageContext.request.contextPath}/" class="standard-logo"><img src="${pageContext.request.contextPath}/images/logo.png" alt="Rentier Logo"></a>
                </div>

            </div><!-- #logo end -->

            <!-- Primary Navigation
            ============================================= -->
            <nav id="primary-menu" class="style-3">

                <ul>

                    <li><a href="/admin/config">
                        <div>Konfiguracja</div>
                    </a>

                    </li>

                    <li><a href="/admin/users">
                        <div>Użytkownicy</div>
                    </a>

                    </li>

                    <li><a href="/admin/products">
                        <div>Produkty</div>
                    </a>

                    </li>

                    <li><a href="/admin/productShops">
                        <div>Stan magazynowy</div>
                    </a>
                    </li>

                    <li><a href="/admin/orders">
                        <div>Zamówienia</div>
                    </a>
                    </li>

                </ul>

                <div id="top-search">
                    <sec:authorize access="!isAuthenticated()">
                        <a href="/login"><i class="icon-user-alt"></i></a>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <a href="/logout"><i class="icon-user-slash"></i></a>
                    </sec:authorize>
                </div>

            </nav><!-- #primary-menu end -->

        </div>

    </div>

</header><!-- #header end -->
