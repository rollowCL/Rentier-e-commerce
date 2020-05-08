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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
            <h1>Użytkownicy</h1>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="row clearfix">

                    <div class="col-lg-12">
                        <div class="filterForm">
                            <form:form class="myFormLeft" action="${pageContext.request.contextPath}/admin/users/filterUsers" method="post" modelAttribute="userRoleFilter">
                                <form:radiobutton path="id" value="0" label="Wszystkie"/>
                                <form:radiobuttons path="id" items="${userRoles}" itemLabel="roleName" itemValue="id"/>
                                <button type="submit" class="button button-mini button-blue button-3d"
                                        value="Submit">Filtruj
                                </button>
                            </form:form>
                            <form class="myFormRight" action="${pageContext.request.contextPath}/admin/users/filterUsersName" method="post">
                                <label for="userNameSearch">Szukaj</label>
                                <input type="text" placeholder="podaj fragment nazwiska" minlength="3" name="userNameSearch" id="userNameSearch" maxlength="20"/>
                                <button type="submit" class="button button-mini button-blue button-3d"
                                        value="Submit">Szukaj
                                </button>
                            </form>
                        </div>

                        <table class="table table-bordered">
                            <thead>
                                <th scope="col">Imię</th>
                                <th scope="col">Nazwisko</th>
                                <th scope="col">Telefon</th>
                                <th scope="col">Email</th>
                                <th scope="col">Rola</th>
                                <th scope="col">Data rejestracji</th>
                                <th scope="col">Aktywny</th>
                                <th scope="col"></th>
                            </thead>
                            <tbody>
                                <c:forEach items="${users}" var="user">
                                    <tr>
                                        <td>${user.firstName}</td>
                                        <td>${user.lastName}</td>
                                        <td>${user.phone}</td>
                                        <td>${user.email}</td>
                                        <td>${user.userRole.roleName}</td>
                                        <td>
                                            <fmt:parseDate value="${ user.registerDate }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                                            <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsedDateTime }" />
                                        </td>
                                        <td>
                                            <c:if test="${user.active}"><i
                                                    class="icon-line-square-check"></i></c:if>
                                            <c:if test="${!user.active}"><i
                                                    class="icon-line-square-cross"></i></c:if>
                                        </td>
                                        <td>
                                            <a class="button button-mini button-red button-3d"
                                               href="${pageContext.request.contextPath}/admin/users/changeRole?userId=${user.id}">Zmień rolę
                                            </a>
                                            <a class="button button-mini button-blue button-3d"
                                               href="${pageContext.request.contextPath}/admin/users/change?userId=${user.id}">
                                                <c:if test="${user.active}">Dezaktywuj</c:if>
                                                <c:if test="${!user.active}">Aktywuj</c:if>
                                            </a>
<%--                                            <c:if test="${user.userRole.orderType.orderTypeName eq 'internal'}">--%>
<%--                                                <a class="button button-mini button-blue button-3d"--%>
<%--                                                   href="/admin/users/shops?userId=${user.id}">Sklepy</a>--%>
<%--                                            </c:if>--%>
                                        </td>
                                    </tr>

                                </c:forEach>
                            </tbody>
                        </table>

                    </div>

                    <div class="w-100 line d-block d-md-none"></div>

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
