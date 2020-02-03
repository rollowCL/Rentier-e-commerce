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
                    <a href="${pageContext.request.contextPath}/" class="standard-logo"><img src="${pageContext.request.contextPath}/images/logo.png" alt="Rentier Logo"></a>
                </div>

            </div><!-- #logo end -->

            <!-- Primary Navigation
            ============================================= -->
            <nav id="primary-menu" class="style-3">

                <ul>
                    <li><a href="${pageContext.request.contextPath}/admin">
                        <div>Home</div>
                    </a>

                    </li>

                    <li><a href="/admin/config">
                        <div>Konfiguracja</div>
                    </a>

                    </li>

                    <li><a href="/admin/users">
                        <div>Użytkownicy</div>
                    </a>

                    </li>

                    <li><a href="/admin/products/">
                        <div>Produkty</div>
                    </a>

                    </li>

                    <li><a href="/admin/productShops/">
                        <div>Stan magazynowy</div>
                    </a>
                    </li>

                    <li><a href="#">
                        <div>Zamówienia</div>
                    </a>
                    </li>

                </ul>

                <div id="top-search">
                    <c:if test="${loggedId == null}"><a href="/login"><i class="icon-user-alt"></i></a></c:if>
                    <c:if test="${loggedId != null}"><a href="/logout"><i class="icon-user-slash"></i></a></c:if>
                </div>

            </nav><!-- #primary-menu end -->

        </div>

    </div>

</header><!-- #header end -->
