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
            <h1>Przypominanie hasła</h1>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="col_one_third nobottommargin">

                    <h3>Podaj nowe hasło</h3>
                    <div class="col-md-12 form-group">
                        <form:form class="row" method="post" action="/resetpassword"
                                   id="resetForm" modelAttribute="user">
                            <form:input type="hidden" path="id"/>
                            <div class="col-md-6 form-group">
                                <form:label path="password">Nowe Hasło</form:label>
                                <form:input type="password" path="password" id="pass1" class="form-control"/>
                                <form:errors path="password" cssClass="error"/>
                            </div>
                            <div class="col-md-6 form-group">
                                <form:label path="password2">Powtórz hasło</form:label>
                                <form:input type="password" path="password2" id="pass2" class="form-control"/>
                                <form:errors path="password2" cssClass="error"/>
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
    </section>
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