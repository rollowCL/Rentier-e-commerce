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
<html dir="ltr" lang="en-US">
<head>

	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="author" content="SemiColonWeb" />

	<!-- Stylesheets
	============================================= -->
	<link href="https://fonts.googleapis.com/css?family=Lato:300,400,400i,700|Raleway:300,400,500,600,700|Crete+Round:400i" rel="stylesheet" type="text/css" />
	<link href='<spring:url value="/css/bootstrap.css"/>' rel="stylesheet"/>
	<link href='<spring:url value="/css/style.css"/>' rel="stylesheet"/>
	<link href='<spring:url value="/css/font-icons.css"/>' rel="stylesheet"/>
	<link href='<spring:url value="/css/animate.css"/>' rel="stylesheet"/>
	<link href='<spring:url value="/css/custom.css"/>' rel="stylesheet"/>
	<link href='<spring:url value="/css/responsive.css"/>' rel="stylesheet"/>
	<link href='<spring:url value="/css/colors.css"/>' rel="stylesheet"/>
	<meta name="viewport" content="width=device-width, initial-scale=1" />

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

					<div class="row clearfix">

						<div class="col-md-12">

<!--							conent-->
							<c:forEach items="${productCategories}" var="productCategory">
                                <c:out value="${productCategory.id} - ${productCategory.categoryName} - ${productCategory.active}"/><br/>
							</c:forEach>


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

	<!-- Go To Top
	============================================= -->
	<div id="gotoTop" class="icon-angle-up"></div>

	<!-- External JavaScripts
	============================================= -->
	<script src="js/jquery.js"></script>
	<script src="js/plugins.js"></script>

	<!-- Footer Scripts
	============================================= -->
	<script src="js/functions.js"></script>

</body>
</html>