// ViveU - Acceso Oculto al Panel Administrativo
// Presionar el logo 4 veces (sin límite de tiempo) para acceder al portal de moderación

(function() {
    // Usamos sessionStorage para que el conteo no se pierda jamás
    function obtenerConteo() {
        return parseInt(sessionStorage.getItem('viveu_admin_clicks') || '0', 10);
    }

    function guardarConteo(num) {
        sessionStorage.setItem('viveu_admin_clicks', num.toString());
    }

    function activarDetectorLogo() {
        // Seleccionamos tanto la imagen del logo como el enlace contenedor
        const logoElementos = document.querySelectorAll('.brand-logo-img, .navbar-brand-centered, .brand-logo, a[href="/"], a[href="./"]');
        
        logoElementos.forEach(elemento => {
            // Solo asociar a elementos que contengan el logo
            if (!elemento.classList.contains('brand-logo-img') && 
                !elemento.classList.contains('navbar-brand-centered') && 
                !elemento.querySelector('.brand-logo-img')) {
                return;
            }

            elemento.addEventListener('click', function(e) {
                // Siempre prevenir la recarga para que los clics se acumulen sin interrupciones
                e.preventDefault();
                e.stopPropagation();

                let clicks = obtenerConteo() + 1;
                guardarConteo(clicks);

                // Micro-animación visual en el logo para que el usuario sienta el toque
                const img = elemento.querySelector('img') || elemento;
                if (img) {
                    img.style.transition = 'transform 0.15s ease';
                    img.style.transform = 'scale(0.88)';
                    setTimeout(() => {
                        img.style.transform = 'scale(1)';
                    }, 150);
                }

                // Vibración táctil si el dispositivo móvil lo soporta
                if (navigator.vibrate) {
                    navigator.vibrate(40);
                }

                console.log("ViveU Secret Admin Click:", clicks, "/ 4");

                if (clicks >= 4) {
                    // Reiniciar el contador antes de redirigir
                    guardarConteo(0);

                    // Redirección inmediata al login secreto
                    window.location.href = '/admin/login';
                    return false;
                }
            });
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', activarDetectorLogo);
    } else {
        activarDetectorLogo();
    }
})();
