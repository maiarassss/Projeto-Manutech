import { createRouter, createWebHistory } from "vue-router";

import InicioView from "../views/Inicio.vue";
import OrdensView from "../views/ordens/OrdemManutencao.vue/index.js";
import TecnicosView from "../views/tecnicos/Tecnicos.vue";
import MaquinasView from "../views/maquinas/Maquinas.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      component: InicioView,
    },
    {
      path: "/ordens",
      component: OrdensView,
    },
    {
      path: "/tecnicos",
      component: TecnicosView,
    },
    {
      path: "/maquinas",
      component: MaquinasView,
    },
    {
      path: "/:pathMatch(.*)*",
      redirect: "/",
    },
  ],
});

export default router;
