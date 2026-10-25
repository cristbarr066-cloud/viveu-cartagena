// ViveU - JavaScript Interactivo y Motor de Filtrado en Tiempo Real

let seleccionComparar = new Set();
let todosLosAlojamientos = [];

document.addEventListener('DOMContentLoaded', () => {
    inicializarEventos();
    cargarDatosIniciales();
    inicializarCalculadora();
});

// Cargar datos desde la API REST
async function cargarDatosIniciales() {
    try {
        const res = await fetch('/api/alojamientos');
        todosLosAlojamientos = await res.json();
        actualizarContador(todosLosAlojamientos.length);
    } catch (e) {
        console.error("Error al cargar alojamientos", e);
    }
}

function inicializarEventos() {
    // Filtros
    const selectSede = document.getElementById('filtroSede');
    const inputPrecio = document.getElementById('filtroPrecio');
    const labelPrecio = document.getElementById('valorPrecioMax');
    const checkVerificados = document.getElementById('filtroVerificados');
    const checkAire = document.getElementById('filtroAire');
    const checkServicios = document.getElementById('filtroServicios');

    if (inputPrecio && labelPrecio) {
        inputPrecio.addEventListener('input', (e) => {
            const val = parseInt(e.target.value);
            labelPrecio.textContent = `$${val.toLocaleString('es-CO')} COP`;
            ejecutarFiltro();
        });
    }

    [selectSede, checkVerificados, checkAire, checkServicios].forEach(el => {
        if (el) el.addEventListener('change', ejecutarFiltro);
    });

    // Comparador
    document.querySelectorAll('.compare-checkbox').forEach(cb => {
        cb.addEventListener('change', handleCompararCheck);
    });

    // Botón comparar flotante
    const btnAbrirComparador = document.getElementById('btnAbrirComparador');
    if (btnAbrirComparador) {
        btnAbrirComparador.addEventListener('click', abrirModalComparar);
    }

    const btnCerrarModal = document.getElementById('btnCerrarModal');
    if (btnCerrarModal) {
        btnCerrarModal.addEventListener('click', cerrarModalComparar);
    }

    // Chat Asistente IA
    const btnToggleIA = document.getElementById('btnToggleIA');
    const btnCerrarIA = document.getElementById('btnCerrarIA');
    const formChatIA = document.getElementById('formChatIA');
    const inputChatIA = document.getElementById('inputChatIA');

    if (btnToggleIA) {
        btnToggleIA.addEventListener('click', () => {
            document.getElementById('aiChatWindow').classList.toggle('open');
        });
    }

    if (btnCerrarIA) {
        btnCerrarIA.addEventListener('click', () => {
            document.getElementById('aiChatWindow').classList.remove('open');
        });
    }

    if (formChatIA) {
        formChatIA.addEventListener('submit', (e) => {
            e.preventDefault();
            enviarMensajeIA(inputChatIA.value);
            inputChatIA.value = '';
        });
    }
}

// Filtro Dinámico
async function ejecutarFiltro() {
    const sede = document.getElementById('filtroSede')?.value || '';
    const precioMax = document.getElementById('filtroPrecio')?.value || 500000;
    const soloVerificados = document.getElementById('filtroVerificados')?.checked || false;
    const aireAcondicionado = document.getElementById('filtroAire')?.checked || false;
    const incluyeServicios = document.getElementById('filtroServicios')?.checked || false;

    const params = new URLSearchParams();
    if (sede) params.append('sede', sede);
    if (precioMax) params.append('precioMax', precioMax);
    if (soloVerificados) params.append('soloVerificados', 'true');
    if (aireAcondicionado) params.append('aireAcondicionado', 'true');
    if (incluyeServicios) params.append('incluyeServicios', 'true');

    try {
        const res = await fetch(`/api/alojamientos?${params.toString()}`);
        const alojamientos = await res.json();
        renderizarTarjetas(alojamientos);
        actualizarContador(alojamientos.length);
    } catch (e) {
        console.error("Error al filtrar", e);
    }
}

function actualizarContador(total) {
    const el = document.getElementById('contadorAlojamientos');
    if (el) el.textContent = `${total} alojamientos encontrados`;
}

function renderizarTarjetas(lista) {
    const contenedor = document.getElementById('gridAlojamientos');
    if (!contenedor) return;

    if (lista.length === 0) {
        contenedor.innerHTML = `
            <div style="grid-column: 1/-1; text-align: center; padding: 3rem; background: var(--bg-card); border-radius: var(--radius-md); border: 1px dashed var(--border-subtle); box-shadow: var(--shadow-sm);">
                <span style="font-size: 2.5rem;">🔍</span>
                <h3 style="margin-top: 1rem; color: #0f172a;">No encontramos habitaciones con esos filtros exactos</h3>
                <p style="color: var(--text-muted); margin-top: 0.5rem;">Intenta aumentar el presupuesto o desactivar filtros de servicios.</p>
                <button onclick="resetearFiltros()" class="btn-outline" style="margin-top: 1.5rem;">Restablecer filtros</button>
            </div>
        `;
        return;
    }

    contenedor.innerHTML = lista.map(a => `
        <div class="alojamiento-card" id="card-${a.id}">
            <div class="card-image-wrap">
                <img src="${a.imagenUrl}" alt="${a.titulo}" class="card-image" loading="lazy">
                <div class="card-badges">
                    ${a.destacado ? '<span class="badge-featured">⭐ Destacado VIP</span>' : ''}
                    ${a.verificadoViveU ? '<span class="badge-verified">✓ Verificado ViveU</span>' : ''}
                </div>
                <div class="card-price-tag">
                    $${Math.round(a.precioMensual).toLocaleString('es-CO')}
                    <span>/mes</span>
                </div>
            </div>
            <div class="card-content">
                <div class="card-location">
                    <span>📍</span> ${a.sector} • ${a.distanciaMinutos} min a ${a.sedeCercana.split('(')[0].trim()}
                </div>
                <h3 class="card-title">${a.titulo}</h3>
                <p class="card-desc">${a.descripcion}</p>
                
                <div class="card-amenities">
                    <span class="amenity-pill">${a.tipoHabitacion}</span>
                    ${a.incluyeServicios ? '<span class="amenity-pill">💡 Servicios incl.</span>' : ''}
                    ${a.wifiFibraOptica ? '<span class="amenity-pill">📶 Wi-Fi Alta Vel.</span>' : ''}
                    ${a.aireAcondicionado ? '<span class="amenity-pill">❄️ Con Aire A/A</span>' : ''}
                    ${a.abanico ? '<span class="amenity-pill">🌀 Abanico</span>' : ''}
                    ${a.cocinaDisponible ? '<span class="amenity-pill">🍳 Cocina</span>' : ''}
                </div>

                <div class="card-footer">
                    <label class="compare-btn-checkbox">
                        <input type="checkbox" class="compare-checkbox" value="${a.id}" 
                               data-img="${a.imagenUrl}" 
                               ${seleccionComparar.has(String(a.id)) ? 'checked' : ''} 
                               onchange="handleCompararCheck(this)">
                        Comparar
                    </label>
                    <button class="btn-primary" style="padding: 0.45rem 0.9rem; font-size: 0.82rem;" 
                            onclick="verDetalleAlojamiento(${a.id})">
                        Ver Detalles
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

function resetearFiltros() {
    document.getElementById('filtroSede').value = '';
    document.getElementById('filtroPrecio').value = 500000;
    document.getElementById('valorPrecioMax').textContent = '$500.000 COP';
    document.getElementById('filtroVerificados').checked = false;
    document.getElementById('filtroAire').checked = false;
    document.getElementById('filtroServicios').checked = false;
    ejecutarFiltro();
}

// Lógica de Comparador Flotante
function handleCompararCheck(inputElement) {
    const id = String(inputElement.value);
    const imgSrc = inputElement.getAttribute('data-img');

    if (inputElement.checked) {
        if (seleccionComparar.size >= 3) {
            alert("Puedes comparar un máximo de 3 alojamientos a la vez.");
            inputElement.checked = false;
            return;
        }
        seleccionComparar.add(id);
    } else {
        seleccionComparar.delete(id);
    }

    actualizarBarraComparar();
}

function actualizarBarraComparar() {
    const bar = document.getElementById('floatingCompareBar');
    const chipsCont = document.getElementById('compareChips');
    const countLabel = document.getElementById('compareCount');

    if (!bar) return;

    if (seleccionComparar.size > 0) {
        bar.classList.add('show');
        countLabel.textContent = `${seleccionComparar.size} seleccionado${seleccionComparar.size > 1 ? 's' : ''}`;
        
        // Renderizar chips con miniaturas
        chipsCont.innerHTML = Array.from(seleccionComparar).map(id => {
            const cb = document.querySelector(`.compare-checkbox[value="${id}"]`);
            const img = cb ? cb.getAttribute('data-img') : '';
            return `<div class="compare-chip"><img src="${img}" alt="habitación"></div>`;
        }).join('');
    } else {
        bar.classList.remove('show');
    }
}

function limpiarComparador() {
    seleccionComparar.clear();
    document.querySelectorAll('.compare-checkbox').forEach(cb => cb.checked = false);
    actualizarBarraComparar();
}

async function abrirModalComparar() {
    if (seleccionComparar.size === 0) return;

    const ids = Array.from(seleccionComparar).map(Number);
    try {
        const res = await fetch('/api/alojamientos/comparar', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(ids)
        });
        const items = await res.json();
        renderizarTablaComparativa(items);
        document.getElementById('modalComparador').classList.add('active');
    } catch (e) {
        console.error("Error al comparar", e);
    }
}

function cerrarModalComparar() {
    document.getElementById('modalComparador').classList.remove('active');
}

function renderizarTablaComparativa(items) {
    const container = document.getElementById('tablaComparativaContainer');
    if (!container) return;

    container.innerHTML = `
        <div class="compare-table-wrapper">
            <table class="compare-table">
            <thead>
                <tr>
                    <th>Característica</th>
                    ${items.map(i => `
                        <th style="color: #0f172a; text-align: center;">
                            <img src="${i.imagenUrl}" style="width: 100%; height: 110px; object-fit: cover; border-radius: var(--radius-sm); margin-bottom: 0.5rem; border: 1px solid #e2e8f0;">
                            <div style="font-weight: 700;">${i.titulo}</div>
                            <div style="font-size: 1.15rem; color: #7c3aed; font-weight: 800; margin-top: 0.25rem;">$${Math.round(i.precioMensual).toLocaleString('es-CO')} COP</div>
                        </th>
                    `).join('')}
                </tr>
            </thead>
            <tbody>
                <tr>
                    <th>Sector / Barrio</th>
                    ${items.map(i => `<td>📍 ${i.sector}</td>`).join('')}
                </tr>
                <tr>
                    <th>Sede Universitaria Cercana</th>
                    ${items.map(i => `<td>🏫 ${i.sedeCercana} (a ${i.distanciaMinutos} min)</td>`).join('')}
                </tr>
                <tr>
                    <th>Tipo de Alojamiento</th>
                    ${items.map(i => `<td>🛏️ ${i.tipoHabitacion}</td>`).join('')}
                </tr>
                <tr>
                    <th>Servicios Públicos (Luz/Agua/Gas)</th>
                    ${items.map(i => `<td>${i.incluyeServicios ? '✅ 100% Incluidos' : '❌ Se cobran por separado'}</td>`).join('')}
                </tr>
                <tr>
                    <th>Climatización</th>
                    ${items.map(i => `<td>${i.aireAcondicionado ? '❄️ Aire Acondicionado' : (i.abanico ? '🌀 Abanico de techo' : 'Ventilación natural')}</td>`).join('')}
                </tr>
                <tr>
                    <th>Conectividad Wi-Fi</th>
                    ${items.map(i => `<td>${i.wifiFibraOptica ? '⚡ Fibra óptica alta velocidad' : 'Básica'}</td>`).join('')}
                </tr>
                <tr>
                    <th>Cocina & Lavandería</th>
                    ${items.map(i => `<td>Cocina: ${i.cocinaDisponible ? 'Sí' : 'No'} | Lavado: ${i.zonaLavanderia ? 'Sí' : 'No'}</td>`).join('')}
                </tr>
                <tr>
                    <th>Normas de Convivencia</th>
                    ${items.map(i => `<td style="font-size: 0.85rem; color: var(--text-sub);">${i.normasConvivencia || 'Consultar con arrendador'}</td>`).join('')}
                </tr>
                <tr>
                    <th>Contacto Directo</th>
                    ${items.map(i => `
                        <td>
                            <a href="https://wa.me/${i.telefonoContacto.replace(/[^0-9]/g, '')}?text=Hola,%20vi%20tu%20habitaci%C3%B3n%20en%20ViveU" 
                               target="_blank" class="btn-primary" style="padding: 0.45rem 0.85rem; font-size: 0.8rem;">
                               💬 WhatsApp Propietario
                            </a>
                        </td>
                    `).join('')}
                </tr>
            </tbody>
        </table>
        </div>
    `;
}

// Detalle individual para el estudiante con Mapa Interactivo
let mapaDetalleInstancia = null;

const SEDES_COORD_MAP = {
    "Sede Zaragocilla (Salud)": { lat: 10.3985, lng: -75.4982, nombre: "Sede Zaragocilla (Medicina/Salud)" },
    "Sede Piedra de Bolívar (Ingeniería)": { lat: 10.4072, lng: -75.5028, nombre: "Sede Piedra de Bolívar (Ingeniería)" },
    "Sede Claustro San Agustín (Centro)": { lat: 10.4236, lng: -75.5498, nombre: "Sede Claustro San Agustín (Centro)" },
    "Sede San Pablo": { lat: 10.3951, lng: -75.4970, nombre: "Sede San Pablo" }
};

async function verDetalleAlojamiento(id) {
    try {
        const res = await fetch(`/api/alojamientos/${id}`);
        const a = await res.json();
        
        const container = document.getElementById('tablaComparativaContainer');
        const latHabitacion = a.latitud || 10.3995;
        const lngHabitacion = a.longitud || -75.4975;
        const direccionTexto = a.direccion ? a.direccion : `${a.sector}, Cartagena`;

        container.innerHTML = `
            <div class="modal-detail-grid">
                <div>
                    <img src="${a.imagenUrl}" style="width: 100%; height: 260px; object-fit: cover; border-radius: var(--radius-md); box-shadow: var(--shadow-card);">
                    
                    <div style="margin-top: 1.25rem;">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.4rem;">
                            <strong style="color: #0f172a; font-size: 0.92rem;">🗺️ Ubicación y Ruta a la Sede:</strong>
                            <span style="font-size: 0.78rem; font-weight: 700; color: #7c3aed; background: rgba(124, 58, 237, 0.08); padding: 0.15rem 0.5rem; border-radius: var(--radius-full);">
                                🚶‍♂️ ~${a.distanciaMinutos} min a pie
                            </span>
                        </div>
                        <div id="mapaDetalleEstudiante" style="height: 220px; width: 100%; border-radius: var(--radius-md); border: 1px solid var(--border-subtle); box-shadow: var(--shadow-sm); z-index: 10;"></div>
                        <div style="font-size: 0.8rem; color: var(--text-muted); margin-top: 0.4rem;">
                            📍 Dirección: <strong>${direccionTexto}</strong>
                        </div>
                    </div>
                </div>

                <div style="display: flex; flex-direction: column;">
                    <div style="display: flex; gap: 0.5rem; margin-bottom: 0.5rem;">
                        ${a.destacado ? '<span class="badge-featured">⭐ Destacado VIP</span>' : ''}
                        ${a.verificadoViveU ? '<span class="badge-verified">✓ Verificado ViveU</span>' : ''}
                    </div>

                    <div style="color: #7c3aed; font-weight: 700; font-size: 0.9rem;">📍 ${a.sector} • A ${a.distanciaMinutos} min de ${a.sedeCercana.split('(')[0].trim()}</div>
                    <h2 style="margin: 0.35rem 0 0.75rem; font-size: 1.55rem; color: #0f172a;">${a.titulo}</h2>
                    
                    <div style="font-size: 1.85rem; font-weight: 800; color: #7c3aed; margin-bottom: 0.75rem;">
                        $${Math.round(a.precioMensual).toLocaleString('es-CO')} <span style="font-size: 0.9rem; color: var(--text-muted); font-weight: 500;">COP / mes</span>
                    </div>

                    <p style="color: var(--text-sub); margin-bottom: 1rem; line-height: 1.5; font-size: 0.9rem;">${a.descripcion}</p>
                    
                    <div class="card-amenities" style="margin-bottom: 1rem;">
                        <span class="amenity-pill">🛏️ ${a.tipoHabitacion}</span>
                        ${a.incluyeServicios ? '<span class="amenity-pill">💡 Servicios incl.</span>' : '<span class="amenity-pill">💡 Servicios por separado</span>'}
                        ${a.wifiFibraOptica ? '<span class="amenity-pill">📶 Wi-Fi Alta Vel.</span>' : ''}
                        ${a.aireAcondicionado ? '<span class="amenity-pill">❄️ Aire Acondicionado</span>' : (a.abanico ? '<span class="amenity-pill">🌀 Abanico</span>' : '')}
                        ${a.cocinaDisponible ? '<span class="amenity-pill">🍳 Derecho a Cocina</span>' : ''}
                        ${a.zonaLavanderia ? '<span class="amenity-pill">🧺 Lavandería</span>' : ''}
                    </div>

                    <div style="background: #f8fafc; border: 1px solid #e2e8f0; padding: 1rem; border-radius: var(--radius-sm); margin-bottom: 1.25rem;">
                        <h4 style="color: #0f172a; margin-bottom: 0.35rem; font-size: 0.9rem;">📋 Normas de Convivencia:</h4>
                        <p style="font-size: 0.85rem; color: var(--text-sub);">${a.normasConvivencia || 'Consultar directamente con el arrendador.'}</p>
                    </div>

                    <div style="margin-top: auto;">
                        <a href="https://wa.me/${a.telefonoContacto.replace(/[^0-9]/g, '')}?text=Hola,%20vi%20la%20habitaci%C3%B3n%20'${encodeURIComponent(a.titulo)}'%20en%20ViveU%20y%20me%20gustar%C3%ADa%20agendar%20una%20visita" 
                           target="_blank" class="btn-primary" style="width: 100%; justify-content: center; padding: 0.85rem 1.5rem; font-size: 0.95rem;">
                           💬 Contactar al Arrendador (${a.nombrePropietario})
                        </a>
                    </div>
                </div>
            </div>
        `;
        document.getElementById('modalComparador').classList.add('active');

        // Renderizar mapa de Leaflet en el modal
        setTimeout(() => {
            if (mapaDetalleInstancia) {
                mapaDetalleInstancia.remove();
            }

            const elemMapa = document.getElementById('mapaDetalleEstudiante');
            if (!elemMapa) return;

            mapaDetalleInstancia = L.map('mapaDetalleEstudiante').setView([latHabitacion, lngHabitacion], 16);

            // Capas Oficiales de Google Maps
            const googleStreets = L.tileLayer('https://mt{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', {
                subdomains: ['0', '1', '2', '3'],
                maxZoom: 20,
                attribution: '&copy; Google Maps'
            }).addTo(mapaDetalleInstancia);

            const googleSat = L.tileLayer('https://mt{s}.google.com/vt/lyrs=y&x={x}&y={y}&z={z}', {
                subdomains: ['0', '1', '2', '3'],
                maxZoom: 20,
                attribution: '&copy; Google Maps Satélite'
            });

            L.control.layers({
                "🗺️ Google Maps": googleStreets,
                "🛰️ Satélite": googleSat
            }, null, { position: 'topright' }).addTo(mapaDetalleInstancia);

            // Icono de casa para habitación
            const iconoCasa = L.divIcon({
                className: 'custom-map-pin',
                html: '<div style="background: #7c3aed; color: white; width: 30px; height: 30px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 14px; border: 2px solid white; box-shadow: 0 4px 8px rgba(0,0,0,0.3);">🏠</div>',
                iconSize: [30, 30],
                iconAnchor: [15, 15]
            });

            const markerH = L.marker([latHabitacion, lngHabitacion], { icon: iconoCasa })
                .addTo(mapaDetalleInstancia)
                .bindPopup(`<b>📍 ${a.titulo}</b><br>${direccionTexto}`);

            // Sede Universitaria con icono de universidad
            const datosSede = SEDES_COORD_MAP[a.sedeCercana] || SEDES_COORD_MAP["Sede Zaragocilla (Salud)"];
            const iconoUniversidad = L.divIcon({
                className: 'custom-sede-pin',
                html: '<div style="background: #1e3a8a; color: white; width: 36px; height: 36px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 19px; border: 3px solid #f59e0b; box-shadow: 0 4px 12px rgba(0,0,0,0.35); position: relative; cursor: pointer;">🎓<span style="position: absolute; bottom: -13px; background: #0f172a; color: #f59e0b; font-size: 9px; font-weight: 800; padding: 1px 5px; border-radius: 4px; white-space: nowrap; border: 1px solid #f59e0b; box-shadow: 0 2px 4px rgba(0,0,0,0.25);">UdeC</span></div>',
                iconSize: [36, 36],
                iconAnchor: [18, 18]
            });

            const markerS = L.marker([datosSede.lat, datosSede.lng], { icon: iconoUniversidad })
                .addTo(mapaDetalleInstancia)
                .bindPopup(`<b>🎓 ${datosSede.nombre}</b><br>Sede Oficial Universidad de Cartagena.`);

            // Línea conectora entre habitación y sede
            const lineaRuta = L.polyline([
                [latHabitacion, lngHabitacion],
                [datosSede.lat, datosSede.lng]
            ], {
                color: '#7c3aed',
                weight: 3,
                dashArray: '6, 8',
                opacity: 0.8
            }).addTo(mapaDetalleInstancia);

            // Ajustar encuadre para mostrar ambos puntos
            const grupoBounds = L.featureGroup([markerH, markerS]);
            mapaDetalleInstancia.fitBounds(grupoBounds.getBounds().pad(0.2));
            mapaDetalleInstancia.invalidateSize();

        }, 250);

    } catch (e) {
        console.error(e);
    }
}

// Asistente IA Estudiantil
async function enviarMensajeIA(texto) {
    if (!texto.trim()) return;

    const chatBody = document.getElementById('aiChatBody');
    
    // Mensaje del usuario
    chatBody.innerHTML += `<div class="ai-msg user">${texto}</div>`;
    chatBody.scrollTop = chatBody.scrollHeight;

    // Loading bot
    const idCarga = 'ia-loading-' + Date.now();
    chatBody.innerHTML += `<div class="ai-msg bot" id="${idCarga}">Buscando habitaciones recomendadas en Cartagena...</div>`;
    chatBody.scrollTop = chatBody.scrollHeight;

    try {
        const res = await fetch('/api/asistente-ia', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({consulta: texto})
        });
        const data = await res.json();
        
        const loader = document.getElementById(idCarga);
        if (loader) loader.remove();

        let tarjetasHTML = '';
        if (data.alojamientos && data.alojamientos.length > 0) {
            tarjetasHTML = `
                <div style="display: flex; flex-direction: column; gap: 0.5rem; margin-top: 0.75rem;">
                    ${data.alojamientos.map(a => `
                        <div style="background: #ffffff; border: 1px solid #cbd5e1; padding: 0.65rem; border-radius: var(--radius-sm); cursor: pointer; box-shadow: 0 2px 6px rgba(0,0,0,0.04);"
                             onclick="verDetalleAlojamiento(${a.id})">
                            <strong style="color: #7c3aed; font-size: 0.84rem;">${a.titulo}</strong>
                            <div style="font-size: 0.78rem; color: #0f172a; font-weight: 600; margin-top: 0.2rem;">$${Math.round(a.precioMensual).toLocaleString('es-CO')} COP • 📍 ${a.sector}</div>
                        </div>
                    `).join('')}
                </div>
            `;
        }

        chatBody.innerHTML += `
            <div class="ai-msg bot">
                ${data.mensaje}
                ${tarjetasHTML}
            </div>
        `;
        chatBody.scrollTop = chatBody.scrollHeight;

    } catch (e) {
        console.error("Error en IA", e);
        const loader = document.getElementById(idCarga);
        if (loader) loader.textContent = "Lo siento, tuve un inconveniente al procesar. Intenta con tu sede o presupuesto.";
    }
}

function usarPromptIA(promptText) {
    document.getElementById('inputChatIA').value = promptText;
    enviarMensajeIA(promptText);
}

// Calculadora de Presupuesto Transcaribe Dinámica y Abierta
function inicializarCalculadora() {
    const inputArriendo = document.getElementById('calcArriendo');
    const inputViajes = document.getElementById('calcViajes');
    const inputDias = document.getElementById('calcDias');
    const inputTarifa = document.getElementById('calcTarifa');
    
    if (inputArriendo && inputViajes) {
        const calcular = () => {
            const arriendo = parseFloat(inputArriendo.value) || 0;
            const viajesPorDia = parseFloat(inputViajes.value) >= 0 ? parseFloat(inputViajes.value) : 0;
            const diasAlMes = parseFloat(inputDias ? inputDias.value : 22) || 0;
            const tarifa = parseFloat(inputTarifa ? inputTarifa.value : 3900) || 3900;
            
            const totalViajes = Math.round(viajesPorDia * diasAlMes);
            const gastoTransporte = totalViajes * tarifa;
            const gastoTotal = arriendo + gastoTransporte;
            
            // Actualización inmediata en pantalla
            const elTotalViajes = document.getElementById('calcTotalViajesMes');
            const elResTransporte = document.getElementById('calcResTransporte');
            const elResTotal = document.getElementById('calcResTotal');
            const elAhorroTexto = document.getElementById('calcAhorroTexto');
            
            if (elTotalViajes) elTotalViajes.textContent = `${totalViajes} viajes al mes`;
            if (elResTransporte) elResTransporte.textContent = `$${Math.round(gastoTransporte).toLocaleString('es-CO')} COP / mes`;
            if (elResTotal) elResTotal.textContent = `$${Math.round(gastoTotal).toLocaleString('es-CO')} COP`;
            if (elAhorroTexto) elAhorroTexto.textContent = `$${Math.round(gastoTransporte).toLocaleString('es-CO')} COP`;
        };

        inputArriendo.addEventListener('input', calcular);
        inputViajes.addEventListener('input', calcular);
        if (inputDias) inputDias.addEventListener('input', calcular);
        if (inputTarifa) inputTarifa.addEventListener('input', calcular);

        calcular();
    }
}
