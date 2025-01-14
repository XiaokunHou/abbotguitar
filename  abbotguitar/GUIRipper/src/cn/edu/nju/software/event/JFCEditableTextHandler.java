/*	
 
 
 * 
 
 *  documentation files (the "Software"), to deal in the Software without restriction, including without 
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *	the Software, and to permit persons to whom the Software is furnished to do so, subject to the following 
 *	conditions:
 * 
 *	The above copyright notice and this permission notice shall be included in all copies or substantial 
 *	portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 *	LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO 
 *	EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER 
 *	IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 *	THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package cn.edu.nju.software.event;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleEditableText;

import cn.edu.nju.software.GuitarModule.GComponent;
import cn.edu.nju.software.GuitarModule.JFCXComponent;
import cn.edu.nju.software.util.Debugger;
 

/**
 * @author     </a>
 */
public class JFCEditableTextHandler extends JFCEventHandler {

	/**
     * 
     */
	public JFCEditableTextHandler() {
		// for(int i =0;i<255*255;i++)
		// GUITAR_DEFAULT_TEXT += Character.toString((char)(i));
	}

	/**
     * 
     */
	private static String GUITAR_DEFAULT_TEXT = "GUITAR DEFAULT TEXT: "
			+ "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
			+ "¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþ"
			+ "ÿĀāĂăĄąĆćĈĉĊċČčĎďĐđĒēĔĕĖėĘęĚěĜĝĞğĠġĢģĤĥĦħĨĩĪīĬĭĮįİıĲĳĴĵĶķĸĹĺĻļĽľĿŀŁłŃńŅņŇňŉŊŋŌōŎŏŐőŒœŔŕŖŗŘřŚśŜŝŞşŠšŢţŤťŦŧŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽžſƀƁƂƃƄƅƆƇƈƉƊƋƌƍƎƏƐƑƒƓƔƕƖƗƘƙƚƛƜƝƞƟƠơƢƣƤƥƦƧƨƩƪƫƬƭƮƯưƱƲƳƴƵƶƷƸƹƺƻƼƽƾƿǀǁǂǃ"
			+ "ǍǎǏǐǑǒǓǔǕǖǗǘǙǚǛǜǝǞǟǠǡǢǣǤǥǦǧǨǩǪǫǬǭǮǯǰ"
			+ "ǾǿȀȁȂȃȄȅȆȇȈȉȊȋȌȍȎȏȐȑȒȓȔȕȖȗȘșȚțȜȝȞȟȠȡȢȣȤȥȦȧȨȩȪȫȬȭȮȯȰȱȲȳȴȵȶȷȸȹȺȻȼȽȾȿɀɁɂɃɄɅɆɇɈɉɊɋɌɍɎɏɐɑɒɓɔɕɖɗɘəɚɛɜɝɞɟɠɡɢɣɤɥɦɧɨɩɪɫɬɭɮɯɰɱɲɳɴɵɶɷɸɹɺɻɼɽɾɿʀʁʂʃʄʅʆʇʈʉʊʋʌʍʎʏʐʑʒʓʔʕʖʗʘʙʚʛʜʝʞʟʠʡʢʣʤʥʦʧʨʩʪʫʬʭʮʯʰʱʲʳʴʵʶʷʸʹ";

	// ";

	@Override
	public void performImpl(GComponent gComponent,Hashtable<String, List<String>> optionalData) {

		List<String> args = new ArrayList<String>();
		args.add(GUITAR_DEFAULT_TEXT);
		performImpl(gComponent, args,optionalData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.nju.software.event.AbstractEventHandler#actionPerformImp(com.sun
	 * .star.accessibility.XAccessible, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void performImpl(GComponent gComponent, Object parameters,Hashtable<String, List<String>> optionalData) {

		if (gComponent == null) {
			return;
		}

		if (parameters instanceof List) {

			List<String> lParameter = (List<String>) parameters;
			String sInputText;
			if (lParameter == null) {
				sInputText = GUITAR_DEFAULT_TEXT;
			} else
				sInputText = (lParameter.size() != 0) ? lParameter.get(0)
						: GUITAR_DEFAULT_TEXT;

			// Accessible aComponent = getAccessible(gComponent);

			// AccessibleContext aContext = aComponent.getAccessibleContext();
			Component component = getComponent(gComponent);

			AccessibleContext aContext = component.getAccessibleContext();
			AccessibleEditableText aTextEvent = aContext

			.getAccessibleEditableText();

			if (aTextEvent == null) {
				System.err.println(this.getClass().getName()
						+ " doesn't support");
				return;
			}
			try {
				aTextEvent.setTextContents(sInputText);
			} catch (Exception e) {
				try {
					Method setText = component.getClass().getMethod("setText",
							String.class);
					setText.invoke(component, sInputText);

					Debugger.pause(GUITAR_DEFAULT_TEXT);

				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}
	}

	/* (non-Javadoc)
	 * @see cn.edu.nju.software.event.GEvent#isSupportedBy(cn.edu.nju.software.GuitarModule.GComponent)
	 */
	@Override
	public boolean isSupportedBy(GComponent gComponent) {
		if (!(gComponent instanceof JFCXComponent))
			return false;
		JFCXComponent jComponent = (JFCXComponent) gComponent;
		Component component = jComponent.getComponent();
		AccessibleContext aContext = component.getAccessibleContext();
		
		if (aContext == null)
			return false;

		Object event;

		// Text
		event = aContext.getAccessibleEditableText();
		if (event != null) {
			return true;
		}
		return false;
	}

}
