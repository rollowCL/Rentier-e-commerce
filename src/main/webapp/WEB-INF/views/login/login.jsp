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
            <h1>Shop</h1>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="#">Home</a></li>
                <li class="breadcrumb-item active" aria-current="page">Shop</li>
            </ol>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="col_one_third nobottommargin">

                    <div class="well well-lg nobottommargin">

                        <h3>Zaloguj się do swojego konta</h3>
                        <form:form class="row" method="post" action="/login"
                                   id="loginForm" modelAttribute="login">
                            <div class="col-md-12 form-group">
                                <form:label path="email">Email</form:label>
                                <form:input path="email" id="email" class="form-control"
                                            maxlength="50"/>
                                <form:errors path="email" cssClass="error"/>
                            </div>
                            <div class="col-md-12 form-group">
                                <form:label path="password">Hasło</form:label>
                                <form:input type="password" path="password" id="password" class="form-control"
                                            maxlength="50"/>
                                <form:errors path="password" cssClass="error"/>
                            </div>
                            <c:out value="${message}"/>
                            <div class="col-12">
                                <button type="submit"
                                        class="button button-mini button-blue button-3d"
                                        value="Submit">Zaloguj
                                </button>
                            </div>
                        </form:form>

                    </div>

                </div>

                <div class="col_two_third col_last nobottommargin">


                    <h3>Nie masz konta? Zarejstruj się.</h3>

                    <form:form class="row" method="post" action="/register"
                               id="registerForm" modelAttribute="user">
                        <form:hidden path="id"/>
                        <div class="col-md-6 form-group">
							<form:label path="firstName">Imię</form:label>
                            <form:input path="firstName" id="firstName" class="form-control"
                                        maxlength="50"/>
                            <form:errors path="firstName" cssClass="error"/>
                        </div>
                        <div class="col-md-6 form-group">
							<form:label path="lastName">Nazwisko</form:label>
                            <form:input path="lastName" id="lastName" class="form-control"
                                        maxlength="50"/>
                            <form:errors path="lastName" cssClass="error"/>
                        </div>
                        <div class="col-md-6 form-group">
							<form:label path="email">Email</form:label>
                            <form:input path="email" id="email" class="form-control"
                                        maxlength="50"/>
                            <form:errors path="email" cssClass="error"/>
                        </div>
                        <div class="col-md-6 form-group">
							<form:label path="phone">Telefon</form:label>
                            <form:input path="phone" id="phone" class="form-control"
                                        maxlength="9"/>
                            <form:errors path="phone" cssClass="error"/>
                        </div>
                        <div class="clear"></div>
                        <div class="col-md-6 form-group">
							<form:label path="password">Hasło</form:label>
                            <form:input type="password" path="password" id="phone" class="form-control"
                                        maxlength="9"/>
                            <form:errors path="password" cssClass="error"/>
                        </div>
                        <div class="col-md-6 form-group">
							<form:label path="password2">Powtórz hasło</form:label>
                            <form:input type="password" path="password2" id="phone" class="form-control"
                                        maxlength="9"/>
                            <form:errors path="password2" cssClass="error"/>
                        </div>
                        <div class="col-12">
                            <button type="submit"
                                    class="button button-mini button-blue button-3d"
                                    value="Submit">Zarejestruj
                            </button>
                        </div>
                    </form:form>

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