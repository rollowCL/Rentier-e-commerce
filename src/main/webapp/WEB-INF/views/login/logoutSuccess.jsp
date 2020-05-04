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
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="author" content="SemiColonWeb" />

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
                <h1>Potwierdzenie wylogowania</h1>
            </div>

        </section><!-- #page-title end -->
		<!-- Content
		============================================= -->
		<section id="content">

			<div class="content-wrap">

				<div class="container clearfix">

					<div class="col-lg-12">

						<div>
							<h3>
								<strong>Zostałeś wylogowany</strong>
								<c:out value="${sessionScope.loggedId}"/>
							</h3>
							<h4>
								<a href="${pageContext.request.contextPath}/login">Zaloguj</a>
							</h4>
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
