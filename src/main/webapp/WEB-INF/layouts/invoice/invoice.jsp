<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%@ page import="hyung.jin.seo.jae.dto.EnrolmentDTO" %>
<%@ page import="hyung.jin.seo.jae.dto.OutstandingDTO" %>
<%@ page import="hyung.jin.seo.jae.utils.JaeConstants" %>

<%
   String invoiceId = request.getParameter("invoiceId");
   String studentId = request.getParameter("studentId");
   String firstName = request.getParameter("firstName");
   String lastName = request.getParameter("lastName");

   Date date = new Date();
   SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
   String today = dateFormat.format(date);

%>

<div class="toolbar no-print">
    <div class="text-right pt-3">
        <form action="/ADM/InvoicePdfV2" id="InvoiceForm" method="post" name="InvoiceForm">
            <input id="InvoiceType" name="InvoiceType" type="hidden" value="D" />
            <input id="StudentNo" name="StudentNo" type="hidden" value="990088" />
            <input id="FromDate" name="FromDate" type="hidden" value="" />
            <input id="ToDate" name="ToDate" type="hidden" value="" />
            <input id="Grade" name="Grade" type="hidden" value="" />
            <input id="Head" name="Head" type="hidden" value="Victoria" />
            <input id="BranchCode" name="BranchCode" type="hidden" value="99" />
            <input data-val="true" data-val-number="The field JobIdx must be a number." data-val-required="The JobIdx field is required." id="JobIdx" name="JobIdx" type="hidden" value="0" />
            <input id="InvoiceNumber" name="InvoiceNumber" type="hidden" value="98994" />
            <input id="Desc" name="Desc" type="hidden" value="" />                
            <button id="emailInvoice" class="btn btn-primary" type="button"><i class="bi bi-envelope"></i> Email</button>
            <button id="printInvoice" class="btn btn-success" type="button"><i class="bi bi-printer"></i> Print</button>
            <button class="btn btn-warning" type="button" id="btnPdf"><i class="bi bi-file-pdf"></i> Export as PDF</button>
        </form>
    </div>
</div>

<div id="invoice">
    <div class="invoice WordSection1" style="min-width: 1080px; padding-top: 35px; padding-bottom: 35px; font-family: 'arial',sans-serif;">
        <table style="width: 90%; margin: 0 auto; border-collapse: collapse; table-layout: fixed; border: 0; color: #444;">
            <tr>
                <td style="vertical-align: middle; padding: 35px 0; text-align: left; font-family: 'arial', sans-serif; font-size: 35px; color:#3f4254; font-weight: 700 !important;background: none; border: 0;">TAX INVOICE</td>
                <td style="width: 450px; padding: 35px 0; vertical-align: middle; border: 0;">
                    <img style="width:450px; vertical-align: top;" src="https://jacelearning.com/Content/invoicelogo.jpg" alt="JAC" />
                    <p style="margin-top: 8px; font-size: 13px;font-weight:600;line-height:1.5">
                        0393610051
                        <br />16c 77-79 Ashley St
                        <br /><span style="font-weight:900;font-size:14px;">ABN 123123123</span>
                    </p>
                </td>
            </tr>
        </table>

        <table style="width: 90%; margin: 0 auto 10px; border-collapse: collapse; table-layout: fixed; border: 0; color: #444;">
            <c:set var="paymentMeta" value="${sessionScope.receiptHeader}" />
            <!-- <c:out value="${paymentMeta}" /> -->
            <tr>
                <td style="width: 100px; font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; font-weight: bold; background: none; border: 0;">Date : </td>
                <td style="font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; background: none; border: 0;font-weight: 600 !important;"><%= today %></td>
                <td style="width: 100px; font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; font-weight: bold; background: none; border: 0;">Due Date : </td>
                <td style="font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; background: none; border: 0;font-weight: 600 !important;"><c:out value="${paymentMeta.registerDate}" /></td>
            </tr>
            <tr>
                <td style="width: 100px; font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; font-weight: bold; background: none; border: 0;">Name : </td>
                <td style="font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; background: none; border: 0;font-weight: 600 !important;"><strong><%= firstName %> <%= lastName %></strong></td>
                <td style="width: 100px; font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; font-weight: bold; background: none; border: 0;">Grade : </td>
                <td style="font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; background: none; border: 0;font-weight: 600 !important;"><c:out value="${paymentMeta.info}" /></td>
            </tr>
            <tr>
                <td style="width: 100px; font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; font-weight: bold; background: none; border: 0;">Student ID : </td>
                <td style="font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; background: none; border: 0;font-weight: 600 !important;"><%= studentId %></td>
                <td style="width: 100px; font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; font-weight: bold; background: none; border: 0;">Invoice No : </td>
                <td style="font-size: 16px; line-height: 2; vertical-align: middle; text-align: left; font-family: 'arial', sans-serif; background: none; border: 0;font-weight: 600 !important;"><%= invoiceId %></td>
            </tr>
        </table>

        <table style="width: 90%; margin: 0 auto; padding: 0; border-collapse: collapse; table-layout: fixed; border: 2px solid #444; color: #444;">
            <colgroup>
                <col style="width:25%;">
                <col style="width:auto">
                <col style="width:10%;">
                <col style="width:15%;">
                <col style="width:10%;">
                <col style="width:15%;">
            </colgroup>
            <thead>
                <tr>
                    <th rowspan="2" style="width:25%; height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: normal; border: 1px solid #444; border-bottom-style:double;border: 1px solid #444;">Title</th>
                    <th rowspan="2" style="width: auto; height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: normal; border: 1px solid #444;border-bottom-style:double;">Period / Date</th>
                    <th colspan="2" style="height: 40px; width:25%; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: normal; border: 1px solid #444;">Fee (Incl.GST)</th>
                    <th rowspan="2" style="width:10%; height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: normal; border: 1px solid #444;border-bottom-style:double;">Discount</th>
                    <th rowspan="2" style="width:15%; height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: normal; border: 1px solid #444;border-bottom-style:double;">Subtotal<br />(Incl.GST)</th>
                </tr>
                <tr>
                    <th style="width:12%; height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: normal; border: 1px solid #444;border-bottom-style:double;">Weeks<br>(Qty)</th>
                    <th style="width:12%; height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: normal; border: 1px solid #444;border-bottom-style:double;">Weekly fee<br>(Unit price)</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="finalTotal" value="0" />
                <c:set var="paidTotal" value="0" />
                <!-- Check if enrolments attribute exists in session -->
                <c:if test="${not empty sessionScope.enrolments}">
                    <!-- Retrieve the payments from session -->
                    <c:set var="enrolments" value="${sessionScope.enrolments}" />
                    <c:forEach items="${enrolments}" var="enrolment">
                        <tr>
                            <td style='height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: bold; border: 1px solid #444;'>Class [<c:out value="${fn:toUpperCase(enrolment.grade)}" />] <c:out value="${enrolment.name}" /></td>
                            <td style='height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: bold; border: 1px solid #444;'><c:out value="${enrolment.extra}" /></td>
                            <c:set var="weeks" value="${enrolment.endWeek - enrolment.startWeek + 1}" />
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'>
                                <c:out value="${weeks}" />
                            </td>
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'><fmt:formatNumber value="${enrolment.price}" pattern="#0.00" /></td>
                            <!-- discount -->
                            <c:set var="discount" value="${enrolment.discount}" />
                            <c:if test="${fn:contains(discount, '%')}">
                                <c:set var="discount" value="${(weeks-enrolment.credit)*enrolment.price*(discount.replace('%', '')/100)}" />
                            </c:if>                          
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'><c:out value="${discount}" /></td>                    
                            <c:set var="totalPrice" value="${((weeks - enrolment.credit) * (enrolment.price)) - discount}" />
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'><fmt:formatNumber value="${totalPrice}" pattern="#0.00" /></td>
                            <!-- Add the amount to the finalTotal variable -->
                            <c:set var="finalTotal" value="${finalTotal + totalPrice}" />
                            <!-- Add the paid to the paidTotal variable. if full paid made, consider paidTotal; otherwise skip now for Outstandings -->
                            <c:if test="${empty sessionScope.outstandings}">
                                <c:set var="paidTotal" value="${paidTotal + enrolment.paid}" />
                            </c:if>
                        </tr>
                        <%--<c:out value="${enrolment}" />--%>
                    </c:forEach>
                </c:if>

                <!-- Check if materials attribute exists in session -->
                <c:if test="${not empty sessionScope.materials}">
                    <!-- Retrieve the payments from session -->
                    <c:set var="materials" value="${sessionScope.materials}" />
                    <c:forEach items="${materials}" var="book">
                        <tr>
                            <td style='height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: bold; border: 1px solid #444;'>Book <c:out value="${book.name}" /></td>
                            <td style='height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: bold; border: 1px solid #444;'></td>
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'></td>
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'></td>
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'></td>
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'><fmt:formatNumber value="${book.price}" pattern="#0.00" /></td>
                            <!-- Add the amount to the finalTotal variable -->
                            <c:set var="finalTotal" value="${finalTotal + book.price}" />
                            <!-- Add the paid to the paidTotal variable. if full paid made, consider paidTotal; otherwise skip now for Outstandings -->
                            <c:if test="${empty sessionScope.materials}">
                                <c:set var="paidTotal" value="${paidTotal + book.price}" />
                            </c:if>
                        </tr>
                         <%--<c:out value="${book}" />--%>
                    </c:forEach>
                </c:if>

                <!-- Check if outstandings attribute exists in session -->
                <c:if test="${not empty sessionScope.outstandings}">
                    <!-- Retrieve the outstandings from session -->
                    <c:set var="outstandings" value="${sessionScope.outstandings}" />
                    <c:forEach items="${outstandings}" var="outstanding">
                        <tr>
                            <td style='height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: bold; border: 1px solid #444;'>
                                <%-- [<c:out value="${fn:toUpperCase(outstanding.invoiceId)}" />] <c:out value="${outstanding.id}" /> --%>
                                Payment
                            </td>
                            <td style='height: 40px; padding: 10px 5px; text-align: center; font-size: 14px; font-weight: bold; border: 1px solid #444;'>
                                <c:out value="${outstanding.registerDate}" />
                            </td>
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'>
                                <%-- <c:out value="${outstanding.paid}" /> --%>
                            </td>
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'>
                                <%-- <c:out value="${outstanding.remaining}" /> --%>
                            </td>
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'>
                                <%-- <c:out value="${outstanding.amount}" /> --%>
                            </td>
                            <td style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: right;'>
                                <!-- <c:out value="${outstanding.paid}" /> -->
                                <c:choose>
                                    <c:when test="${outstanding.paid >= 0}">
                                        <fmt:formatNumber value="${- outstanding.paid}" pattern="#0.00" />
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${outstanding.paid}" pattern="#0.00" />
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <!-- Add the paid to the paidTotal variable -->
                            <c:set var="paidTotal" value="${paidTotal + outstanding.paid}" />
                        </tr>
                    </c:forEach>
                </c:if>


                <c:if test="${not empty sessionScope.invoiceInfo}">
                    <c:set var="invoiceInfo" value="${sessionScope.invoiceInfo}" />
                    <tr>
                        <td colspan='6' style='height: 40px; padding: 10px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: left;'> &#8251; Other Information : <c:out value="${invoiceInfo}" /></td>
                    </tr>
                </c:if>

                <!-- <tr>
                    <td colspan='6' style='height: 40px; padding: 10px 5px; font-size: 14px; font-weight: bold; border: 1px solid #444; text-align: left;'></td>
                </tr> -->

            </tbody>
        </table>

        <table style="width: 90%; margin: 50px auto 0; text-align: right; border-collapse: collapse; table-layout: fixed; border: 0; color: #444;">
            <tr>
                <td style="height: 32px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: right; font-weight: bold; font-family: 'arial', sans-serif; border: 0;font-weight: 600 !important;">FINAL TOTAL</td>
                <td style="height: 32px; width: 100px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: center; color: #bdbdbd; font-style: normal; font-family: 'arial', sans-serif; border: 0;">$</td>
                <td style="height: 32px; width: 130px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: right; font-family: 'arial', sans-serif; border: 0;"><strong><fmt:formatNumber value="${finalTotal}" pattern="#0.00" /></strong></td>
            </tr>
            <!--<tr>
                <td style="height: 32px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: right; font-weight: bold; font-family: 'arial', sans-serif; border: 0;font-style: italic;">D.S Count</td>
                <td style="height: 32px; width: 100px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: center; color: #bdbdbd; font-style: normal; font-family: 'arial', sans-serif; border: 0;">$</td>
                <td style="height: 32px; width: 130px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: right; font-family: 'arial', sans-serif; border: 0;"><strong>{{ICT_DC}}</strong></td>
            </tr>-->
            <tr>
                <td style="height: 32px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: right; font-weight: bold; font-family: 'arial', sans-serif; border: 0;font-weight: 600 !important;">FEE PAID</td>
                <td style="height: 32px; width: 100px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: center; color: #bdbdbd; font-style: normal; font-family: 'arial', sans-serif; border: 0;">$</td>
                <td style="height: 32px; width: 130px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: right; font-family: 'arial', sans-serif; border: 0;">
                    <c:choose>
                        <c:when test="${paidTotal > 0}">
                            <fmt:formatNumber value="${- paidTotal}" pattern="#0.00" />
                        </c:when>
                        <c:otherwise>
                            <fmt:formatNumber value="${paidTotal}" pattern="#0.00" />
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td style="height: 32px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: right; font-weight: bold; font-family: 'arial', sans-serif; border: 0;font-weight: 600 !important;">BALANCE</td>
                <td style="height: 32px; width: 100px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: center; color: #bdbdbd; font-style: normal; font-family: 'arial', sans-serif; border: 0;">$</td>
                <td style="height: 32px; width: 130px; font-size: 15px; line-height: 1.5; vertical-align: top; text-align: right; font-family: 'arial', sans-serif; border: 0;">
                    <!-- Check if the balance is full paid -->
                    <c:choose>
                        <c:when test="${finalTotal - paidTotal <= 0}">
                            PAID IN FULL
                        </c:when>
                        <c:otherwise>
                            <fmt:formatNumber value="${finalTotal - paidTotal}" pattern="#0.00" />
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>

        <table style="width: 90%; margin: 150px auto 10px; text-align: left; border-collapse: collapse; table-layout: fixed; border: 0; color: #444;">
            <!--<tr>
                <td style="font-size: 14px; line-height: 1.6; font-weight: bold; border: 0;">
                    <p style="margin: 0; padding: 0; line-height: 1.8;">• Payment Options : CASH / EFTPOS (50 cents Surcharge) / CREDIT CARD (1.5% Surcharge) / Bank Transfer</p>
                    <p style="margin: 0; padding: 0; line-height: 1.8;">• Bank Details : {{ICT_BANKDETAILS}}</p>
                </td>
            </tr>-->
        </table>
        <table style="width: 90%; margin: 0 auto; text-align: left; border-collapse: collapse; table-layout: fixed; border: 1px solid #444; color: #444;">
            <tr>
                <td style=" padding: 10px 10px 0; border: 0;"><strong style="font-size: 16px;font-style: italic;">Note : </strong></td>
            </tr>
            <tr>
                <td style="font-size: 15px; line-height: 1.6; border: 0; padding: 0 10px 10px;">
                    <!-- <p style="margin: 0; padding: 0; line-height: 1.8;">1.5% surcharge on credit card payment<br />0.5% surcharge on EFTPOS payment<br /><br />Test test test<br />Bank transfer<br />123-456<br />1234 5678</p> -->
                    <p style="margin: 0; padding: 0; line-height: 1.8;">
                        1.5% surcharge on credit card payment<br />0.5% surcharge on EFTPOS payment<br /><br />Test test test<br />Bank transfer<br />123-456<br />1234 5678
                    </p>
                </td>
            </tr>
        </table>
    </div>
</div>

<script>
    $('#printInvoice').click(function () {
        var html = $('.invoice')[0].outerHTML;
        var htmljac = html.replace('TAX INVOICE', 'TAX INVOICE(JAC)');
        var htmlstudent = html.replace('TAX INVOICE', 'TAX INVOICE Receipt(Student)');

        Popup(htmljac);

        //Popup(htmljac +"<div style='page-break-before:always;height:150px;'></div>" + htmlstudent);
        function Popup(data) {

            var mywindow = window.open('', 'PRINT', 'height=800,width=900');
            mywindow.document.write(data);

            mywindow.document.close();
            mywindow.focus();

            mywindow.print();
            mywindow.close();

            return true;
        }
    });


    $('#btnPdf').click(function () {
        $('#InvoiceForm').attr('action', '/ADM/InvoicePdfV2');
        $('#InvoiceForm').submit();
    });

    $('#btnDocx').click(function () {
        $('#InvoiceForm').attr('action', '/ADM/InvoiceWordV2');
        $('#InvoiceForm').submit();
    });

    $('#emailInvoice').click(function () {
        $("#lySpinner").show();
        $.ajax({
            type: "POST",
            url: "/ADM/InvoiceSendEmail",
            data: $('#InvoiceForm').serialize(),
            //contentType: "application/json; charset=utf-8",
            //dataType: "json",
            success: function (response) {
                if (response.success === true) {
                    alert('Transfer Completed');
                    $("#lySpinner").hide();
                }
                else {
                    alert(response.msg);
                    $("#lySpinner").hide();
                }
            },
            error: function (xhr) {
                alert('Transfer Failed.');
                $("#lySpinner").hide();
                //jqxAlert.alert("Something seems Wrong in Deleting your data.");
            }
        });
    });

</script>