console.log("I am javascript!!!");

$(document).ready(function () {


    if($('.sidebar-header h3').css('padding','15px')){
        $('#sidebarCollapse').on('click', function () {
            $('nav .sidebar-header h3').css('padding','0');
            $('#sidebar,#content').toggleClass('active');
            //$('.collapse.in').toggleClass('in');
            //$('a[aria-expanded=true]').attr('aria-expanded', 'false');
        });
    }

    if($('.sidebar-header h3').css('padding','0')){
        $('#sidebarCollapse').on('click', function () {
            $('nav .sidebar-header').css('padding','15px');
            $('#sidebar,#content').toggleClass('inactive');
        });
    }

});


tinymce.init({
    selector: 'textarea#basic-example',
    height: 250,
    width:705,
    menubar: false,
    plugins: [
        'advlist autolink lists link image charmap print preview anchor',
        'searchreplace visualblocks code fullscreen',
        'insertdatetime media table paste code help wordcount'
    ],
    toolbar: 'undo redo | formatselect | ' +
        'bold italic backcolor | alignleft aligncenter ' +
        'alignright alignjustify | bullist numlist outdent indent | ' +
        'removeformat | help',
    content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }'
});
