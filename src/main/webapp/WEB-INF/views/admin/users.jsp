<%--
  Created by IntelliJ IDEA.
  User: osint
  Date: 2020-02-01
  Time: 1:23 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="tab-content clearfix" id="tab-admin-users">
    <div class="card">

        <div class="card-body">
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
    </div>
</div>
