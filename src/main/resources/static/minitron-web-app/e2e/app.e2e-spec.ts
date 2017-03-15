import { MinitronWebAppPage } from './app.po';

describe('minitron-web-app App', () => {
  let page: MinitronWebAppPage;

  beforeEach(() => {
    page = new MinitronWebAppPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
