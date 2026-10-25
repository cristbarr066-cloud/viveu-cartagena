// ViveU - Acceso Oculto al Panel Administrativo
// Presionar el logo 4 veces consecutivas para acceder al portal de moderación

(function() {
    let clickCount = 0;
    let timer = null;

    function activarDetectorLogo() {
        const logos = document.querySelectorAll('.brand-logo-img, .navbar-brand-centered, .brand-logo');
        
        logos.forEach(logo => {
            logo.addEventListener('click', function(e) {
                clickCount++;
                clearTimeout(timer);

                if (clickCount >= 4) {
                    e.preventDefault();
                    e.stopPropagation();
                    clickCount = 0;
                    
                    // Efecto sutil visual al activar el acceso oculto
                    document.body.style.transition = 'opacity 0.25s ease';
                    document.body.style.opacity = '0.7';
                    
                    window.location.href = '/admin/login';
                    return false;
                }

                // Resetear si no se alcanzan 4 clics en 2.2 segundos
                timer = setTimeout(() => {
                    clickCount = 0;
                }, 2200);
            });
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', activarDetectorLogo);
    } else {
        activarDetectorLogo();
    }
})();
