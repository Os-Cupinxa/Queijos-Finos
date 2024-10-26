function verMapa(latitude, longitude) {
    const iframe = document.getElementById("mapaIframe");
    iframe.src = `https://maps.google.com/maps?width=100%&height=300&hl=en&q=${latitude},${longitude}&t=&z=13&ie=UTF8&iwloc=B&output=embed`;
}
