
$('#prime').click(function() {
    toggleFab();
});

//Toggle chat and links
function toggleFab() {
    // $('#chatSend').toggleClass('is-visible')
    // $('.prime').toggleClass('zmdi-comment-outline');
    // $('.prime').toggleClass('zmdi-close');
    // $('.prime').toggleClass('is-active');
    // $('.prime').toggleClass('is-visible');
    $('#prime').toggleClass('is-float');
    $('.chat').toggleClass('is-visible');
    $('.container').toggleClass('is-active');
}