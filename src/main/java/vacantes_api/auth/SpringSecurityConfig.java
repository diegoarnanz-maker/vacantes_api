package vacantes_api.auth;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuración general de seguridad para la API.
 * Define los filtros de seguridad, autorización de rutas y política CORS.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

        /**
         * Bean para codificar contraseñas utilizando BCrypt.
         *
         * @return instancia de PasswordEncoder.
         */
        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        /**
         * Bean que proporciona el AuthenticationManager necesario para autenticación.
         *
         * @param authenticationConfiguration configuración de autenticación.
         * @return AuthenticationManager configurado.
         * @throws Exception si no puede crearse el gestor.
         */
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        /**
         * Configura la cadena de filtros de seguridad HTTP.
         * Define rutas públicas, autenticadas y protegidas por roles.
         *
         * @param http configuración de seguridad HTTP.
         * @return filtro de seguridad configurado.
         * @throws Exception en caso de error de configuración.
         */
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .cors(Customizer.withDefaults())
                                .authorizeHttpRequests(authorize -> authorize

                                                // Swagger
                                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
                                                                "/swagger-ui.html")
                                                .permitAll()

                                                // Autenticación
                                                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/auth/me", "/auth/me1", "/auth/me2")
                                                .authenticated()

                                                // Categorías (público y admin)
                                                .requestMatchers(HttpMethod.GET, "/categorias", "/categorias/{id}",
                                                                "/categorias/buscar/{nombre}")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/categorias")
                                                .hasAuthority("ROLE_ADMON")
                                                .requestMatchers(HttpMethod.PUT, "/categorias/{id}")
                                                .hasAuthority("ROLE_ADMON")
                                                .requestMatchers(HttpMethod.DELETE, "/categorias/{id}")
                                                .hasAuthority("ROLE_ADMON")

                                                // Vacantes (público, empresa)
                                                .requestMatchers(HttpMethod.GET,
                                                                "/vacantes", "/vacantes/{id}",
                                                                "/vacantes/buscar/{nombre}",
                                                                "/vacantes/categoria/{idCategoria}",
                                                                "/vacantes/salario/{salario}",
                                                                "/vacantes/empresa/{nombre}")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/vacantes")
                                                .hasAuthority("ROLE_EMPRESA")
                                                .requestMatchers(HttpMethod.PUT, "/vacantes/{id}")
                                                .hasAuthority("ROLE_EMPRESA")
                                                .requestMatchers(HttpMethod.DELETE, "/vacantes/{id}")
                                                .hasAuthority("ROLE_EMPRESA")
                                                .requestMatchers(HttpMethod.GET, "/vacantes/propias")
                                                .hasAuthority("ROLE_EMPRESA")

                                                // Empresas (admin)
                                                .requestMatchers(HttpMethod.GET, "/empresas", "/empresas/{id}",
                                                                "/empresas/buscar/{nombre}")
                                                .hasAuthority("ROLE_ADMON")
                                                .requestMatchers(HttpMethod.POST, "/empresas/register")
                                                .hasAuthority("ROLE_ADMON")
                                                .requestMatchers(HttpMethod.PUT,
                                                                "/empresas/{id}", "/desactivar/{id}", "/activar/{id}")
                                                .hasAuthority("ROLE_ADMON")
                                                .requestMatchers(HttpMethod.DELETE, "/empresas/{id}")
                                                .hasAuthority("ROLE_ADMON")
                                                .requestMatchers(HttpMethod.GET, "/empresas/desactivadas")
                                                .hasAuthority("ROLE_ADMON")

                                                // Solicitudes (cliente y empresa)
                                                .requestMatchers(HttpMethod.GET, "/solicitudes/mis-solicitudes")
                                                .hasAuthority("ROLE_CLIENTE")
                                                .requestMatchers(HttpMethod.POST, "/solicitudes")
                                                .hasAuthority("ROLE_CLIENTE")
                                                .requestMatchers(HttpMethod.DELETE, "/solicitudes/{id}")
                                                .hasAuthority("ROLE_CLIENTE")
                                                .requestMatchers(HttpMethod.GET, "/solicitudes/vacante/{idVacante}")
                                                .hasAuthority("ROLE_EMPRESA")
                                                .requestMatchers(HttpMethod.PUT,
                                                                "/solicitudes/adjudicar/{id}",
                                                                "/solicitudes/desadjudicar/{id}")
                                                .hasAuthority("ROLE_EMPRESA")

                                                // Usuarios (perfil y administración)
                                                .requestMatchers(HttpMethod.PUT, "/usuarios/perfil").authenticated()
                                                .requestMatchers(HttpMethod.PUT, "/usuarios/perfil/empresa")
                                                .hasAuthority("ROLE_EMPRESA")
                                                .requestMatchers(HttpMethod.GET,
                                                                "/usuarios", "/usuarios/{id}",
                                                                "/usuarios/buscar/nombre/{nombre}",
                                                                "/usuarios/buscar/rol/{rol}",
                                                                "/usuarios/buscar/estado/{estado}")
                                                .hasAuthority("ROLE_ADMON")
                                                .requestMatchers(HttpMethod.PUT,
                                                                "/usuarios/{id}", "/usuarios/{email}",
                                                                "/usuarios/desactivar/{id}", "/usuarios/activar/{id}")
                                                .hasAuthority("ROLE_ADMON")

                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .httpBasic(Customizer.withDefaults());

                return http.build();
        }

        /**
         * Configuración global de CORS para permitir peticiones desde el frontend.
         *
         * @return fuente de configuración CORS.
         */
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("http://localhost:4200"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
