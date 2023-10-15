export class Util {

  public static obterCodigoUsuario(): number {
    const dadosStr: any = localStorage.getItem('usuario');
    if(dadosStr) {
      const dadosUsuario: any = JSON.parse(dadosStr);
      return dadosUsuario.id;
    }
    return 0;
  }

}
