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
            <h1>Użytkownicy</h1>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="#">Admin</a></li>
                <li class="breadcrumb-item active" aria-current="page">Użytkownicy</li>
            </ol>
        </div>

    </section><!-- #page-title end -->
    <!-- Content
    ============================================= -->
    <section id="content">

        <div class="content-wrap">

            <div class="container clearfix">

                <div class="row clearfix">


                    <div class="col-lg-12">

                        <form method="">
                            <input type="radio" name="roleSelect" value="1"/>Klient
                            <input type="radio" name="roleSelect" value="2"/>Sprzedawca
                            <input type="radio" name="roleSelect" value="3"/>Administrator
                        </form>
                        <p class="card-text">
                        <table class="table table-bordered">
                            <thead>
                            <th scope="col">Imię</th>
                            <th scope="col">Nazwisko</th>
                            <th scope="col">Telefon</th>
                            <th scope="col">Email</th>
                            <th scope="col">Rola</th>
                            <th scope="col">Ostatnie logowanie</th>
                            <th scope="col">Aktywny</th>
                            <th scope="col">Obsługiwane sklepy</th>
                            <th scope="col"></th>
                            </thead>
                            <tbody>
                            <tr>
                                <td>Anna</td>
                                <td>Kowalska</td>
                                <td>500500500</td>
                                <td>anna@o2.pl</td>
                                <td>Sprzedawca</td>
                                <td>2020-01-01 23:59:59</td>
                                <td><i class="icon-line-square-check"></i></td>
                                <td>
                                    <select multiple disabled>
                                        <option value="1" selected>Zawiercie,
                                            Marszałkowska
                                        </option>
                                        <option value="2">Zawiercie, Powstańców</option>
                                        <option value="3" selected>Zawiercie, Żabki</option>
                                    </select>
                                </td>
                                <td>
                                    <button class="button button-mini noborder button-red button-3d">
                                        Usuń
                                    </button>
                                    <button class="button button-mini noborder button-blue button-3d">
                                        Edytuj
                                    </button>
                                </td>
                            </tr>
                            <div id="user-edit-1">
                                <tr class="product-edit">
                                    <form class="row">
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td>
                                            <select name="userRole"
                                                    class="form-control required">
                                                <option value="1">Klient</option>
                                                <option value="2" selected>Sprzedawca
                                                </option>
                                                <option value="3">Administrator</option>
                                            </select>
                                        </td>
                                        <td></td>
                                        <td><input type="checkbox" name="userActive"
                                                   checked/></td>
                                        <td>
                                            <select multiple name="userShops">
                                                <option value="1" selected>Zawiercie,
                                                    Marszałkowska
                                                </option>
                                                <option value="2">Zawiercie, Powstańców
                                                </option>
                                                <option value="3" selected>Zawiercie,
                                                    Żabki
                                                </option>
                                            </select>
                                        </td>
                                        <td>
                                            <button class="button button-mini noborder button-green button-3d">
                                                Zapisz
                                            </button>
                                        </td>
                                    </form>
                                </tr>
                            </div>
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

<!-- JavaScripts
    <jsp:include page="../scripts.jsp"/>

</body>
</html>