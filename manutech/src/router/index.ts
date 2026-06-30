import { createRouter, createWebHistory } from "vue-router";

import InicioView from "../views/Inicio.vue";
import OrdensView from "../views/ordens/OrdemManutencao.vue";
import TecnicosView from "../views/tecnicos/Tecnicos.vue";
import MaquinasView from "../views/maquinas/Maquinas.vue";
import MaquinasCadastro from "../views/maquinas/MaquinasCadastro.vue";
import TecnicosCadastro from "@/views/tecnicos/TecnicosCadastro.vue";
import OrdensCadastrar from "../views/ordens/OrdensCadastrar.vue";
import LoginView from "../views/Login.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      component: InicioView,
    },
    {
      path: "/login",
      component: LoginView,
    },
    {
      path: "/ordens",
      component: OrdensView,
    },
    {
      path: "/ordens/cadastro",
      component: OrdensCadastrar,
    },
    {
      path: "/ordens/editar/:id",
      component: OrdensCadastrar,
    },
    {
      path: "/tecnicos",
      component: TecnicosView,
    },
    {
      path: "/tecnicos/cadastro",
      component: TecnicosCadastro,
    },
    {
      path: "/tecnicos/editar/:id",
      component: TecnicosCadastro,
    },
    {
      path: "/maquinas",
      component: MaquinasView,
    },
    {
      path: "/maquinas/cadastro",
      component: MaquinasCadastro,
    },
    {
      path: "/maquinas/editar/:id",
      component: MaquinasCadastro,
    },
    {
      path: "/:pathMatch(.*)*",
      redirect: "/",
    },
  ],
});

export default router;
